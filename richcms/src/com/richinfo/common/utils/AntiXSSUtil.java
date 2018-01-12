package com.richinfo.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class AntiXSSUtil
{
  private static Logger logger = Logger.getLogger(AntiXSSUtil.class);

  static final String[] replace_before = { "\\u", "\\x", "&#", "&amp;#", "\\00" };

  static final String[] replace_after = { "", "", "", "", "" };

  static final Map<String, String> REPL_MAP = new HashMap<String, String>();
  static final List<Tag> tagList = new ArrayList<Tag>();
  static final String cssRemark_s = "/*";
  static final String cssRemark_e = "*/";
  static final String style_s = "<style";
  static final String style_e = "</style";
  static final String img_s = "<img";
  static final String img_e = ">";
  static final String j_s = "<";
  static final String j_e = ">";
  static final String embed_s = "<embed";
  static final String embed_e = ">";
  private static final Pattern EVENT_P = Pattern.compile("[^\\w]on[^=]*=", 2);

  private static final Pattern BROKE_UP_P = Pattern.compile("(\\\\[^\"'])");
  private static final List<Pattern> PA_LIST = new ArrayList<Pattern>();

  protected static void setTagRepl()
  {
    tagList.add(new Tag("<script", "</script", true, "<scrip_tx >"));
    tagList.add(new Tag("<script", ">", false, "<scrip_ty >"));
    tagList.add(new Tag("<xml", "</xml", true, "<xm_x >"));
    tagList.add(new Tag("<xml", ">", false, "<xm_ly >"));
    tagList.add(new Tag("<?xml", "?>", false, "<xm_lx >"));
    tagList.add(new Tag("<svg", "</svg", true, "<sv_gx >"));
    tagList.add(new Tag("<svg", ">", false, "<sv_gy >"));
    tagList.add(new Tag("<x:template", "</x:template", true, "<rmx_x >"));
    tagList.add(new Tag("<meta", ">", false, "<met_ax >"));
    tagList.add(new Tag("<link", ">", false, "<lin_kx >"));
    tagList.add(new Tag("<applet", ">", false, "<apple_tx >"));
    tagList.add(new Tag("<blink", ">", false, "<blin_kx >"));
    tagList.add(new Tag("<bgsound", ">", false, "<bgsoun_dx >"));
    tagList.add(new Tag("<layer", ">", false, "<laye_ry >"));
    tagList.add(new Tag("<ilayer", ">", false, "<ilaye_ry >"));
    tagList.add(new Tag("<base", ">", false, "<bas_ey >"));
    tagList.add(new Tag("<frameset", "</frameset", true, "<framese_tx >"));
    tagList.add(new Tag("<frameset", ">", false, "<framese_ty >"));
    tagList.add(new Tag("<frame", "</frame", true, "<fram_ex >"));
    tagList.add(new Tag("<frame", ">", false, "<fram_ey >"));
    tagList.add(new Tag("<iframe", "</iframe", true, "<ifram_ex >"));
    tagList.add(new Tag("<iframe", ">", false, "<ifram_ey >"));
  }

  public static String antiXSSNEW(String content)
  {
    if (StringUtils.isNotEmpty(content)) {
      content = content.replaceAll("[\\x00-\\x08\\x0b-\\x0c\\x0e-\\x19\\xa0 ᠎᠏ -​    　﻿]", "");
      StringBuilder src = new StringBuilder(content);
      try {
        _antiXSS(content, src);
      } catch (Exception e) {
        logger.error("XSS_anti_error", e);
      }
      return src.toString().replaceAll("<[\\u0000\\s]+script>", "<script>");
    }
    return content;
  }

  public static void _antiXSS(String content, StringBuilder src)
  {
    StringBuilder low = new StringBuilder(content.toLowerCase());

    for (Tag tag : tagList) {
      filterTagAndRepl(src, low, tag);
    }

    StringBuilder block = new StringBuilder(512);

    filterStyle(src, block, low);

    filterIMG(src, block, low);

    filterOther(src, block, low);
    low = null;
    block = null;
  }

  static void filterEmbed(StringBuilder src, StringBuilder block, StringBuilder low)
  {
    int start = 0; int end = -1; int t = 0; int index = 0;

    while ((t++ < 1000) && ((start = low.indexOf("<embed", index)) != -1))
    {
      if ((end = low.indexOf(">", start)) == -1) break;
      index = end + 1;
      block.delete(0, block.length());
      block.append(low.substring(start, end + 1));

      if (block.length() > 0)
      {
        clearBlockUp(low, src, block, start);
        int s = 0;

        if (((s = block.indexOf("src=")) != -1) && (block.lastIndexOf("data:", end) != -1))
        {
          low.replace(start + s, end, "");
          src.replace(start + s, end, "");
          index -= start + s - end;
        }
      }
    }
  }

  private static void filterIMG(StringBuilder src, StringBuilder block, StringBuilder low) {
    int start = 0; int end = -1; int t = 0; int index = 0;
    try {
      while ((t++ < 1000) && ((start = low.indexOf("<img", index)) != -1) && 
        ((end = low.indexOf(">", start)) != -1)) {
        index = end + 1;
        block.delete(0, block.length());
        block.append(low.substring(start, end + 1));

        if (block.length() > 0)
        {
          clearBlockUp(low, src, block, start);
          int s = 0;

          if (((s = block.indexOf("src=", start)) != -1) && (block.indexOf("redirect", s) > 0))
          {
            low.replace(start + s, end, "");
            src.replace(start + s, end, "");
            index -= start + s - end;
          } else if ((s = block.indexOf("xmlns=")) != -1) {
            low.replace(start + s, end, "");
            src.replace(start + s, end, "");
            index -= start + s - end;
          }
        }
      }
    } catch (Exception e) { logger.error("filterIMG", e); }

  }

  private static void filterOther(StringBuilder src, StringBuilder block, StringBuilder low)
  {
    int start = 0; int end = -1; int t = 0; int index = 0;

    while ((t < 1000) && ((start = low.indexOf("<", index)) != -1))
    {
      if ((end = low.indexOf(">", start)) == -1) break;
      t++;
      index = end + 1;
      block.delete(0, block.length());
      block.append(low.substring(start, end + 1));

      if (block.length() > 0)
      {
        if (block.indexOf("style=") != -1) {
          index = clearCssRemark(src, block, low, index, start);
        }

        clearBlockUp(low, src, block, start);

        index = clearBlackList(src, block, low, index, start);

        clearOnEvent(low, src, block, start);
      }
    }
  }

  private static void filterStyle(StringBuilder src, StringBuilder block, StringBuilder low)
  {
    int start = 0; int end = -1; int times = 0; int index = 0;
    try
    {
      while ((times < 1000) && ((start = low.indexOf("<style", index)) != -1))
      {
        if ((end = low.indexOf("</style", start + 6)) == -1) break;
        times++;
        index = end + 7;
        block.delete(0, block.length());
        block.append(low.substring(start, end));

        index = clearCssRemark(src, block, low, index, start);

        clearBlockUp(low, src, block, start);

        index = clearBlackList(src, block, low, index, start);
      }
    } catch (Exception e) {
      logger.error("filterStyle", e);
    }
  }

  private static void filterTagAndRepl(StringBuilder src, StringBuilder low, Tag tag)
  {
    try {
      int start = 0; int end = -1; int t = 0;
      int index = 0;
      while ((t++ < 1000) && ((start = low.indexOf(tag.tagStart, index)) != -1))
      {
        if ((end = low.indexOf(tag.tagEnd, start + tag.tagStartLen)) == -1) break;
        end += tag.tagEndLen;
        low.replace(start, end, tag.tagRepl);
        src.replace(start, end, tag.tagRepl);
        index = start + tag.tagRepl.length();
      }

    }
    catch (Exception e)
    {
      logger.error(new StringBuilder().append("tag_s=").append(tag.tagStart).append("| tag_e=").append(tag.tagEnd).toString(), e);
    }
  }

  private static int clearCssRemark(StringBuilder src, StringBuilder block, StringBuilder low, int index, int start)
  {
    int theIndex = 0; int times = 0;
    try {
      while ((times < 1000) && ((theIndex = block.indexOf("/*")) != -1))
      {
        int ss = theIndex;
        int ee = -1;
        if ((theIndex = block.indexOf("*/", ss)) == -1) break;
        ee = theIndex;
        low.replace(start + ss, start + ee + 2, "");
        src.replace(start + ss, start + ee + 2, "");
        block.replace(ss, ee + 2, "");
        theIndex = ss;
        index -= ee + 2 - ss;
        times++;
      }

    }
    catch (Exception e)
    {
      logger.error("clearCssRemark", e);
    }
    return index;
  }

  private static int clearBlackList(StringBuilder src, StringBuilder block, StringBuilder low, int index, int s1)
  {
    int t = 0; int s3 = 0; int e3 = 0;
    try {
      for (int i = 0; i < replace_before.length; i++) {
        while ((t++ < 1000) && ((s3 = block.indexOf(replace_before[i])) != -1))
        {
          e3 = s3 + replace_before[i].length();
          low.replace(s1 + s3, s1 + e3, replace_after[i]);
          src.replace(s1 + s3, s1 + e3, replace_after[i]);
          block.replace(s3, e3, replace_after[i]);
          index -= e3 - s3 - replace_after[i].length();
        }
        t = 0;
      }

      Matcher m = null;
      Iterator<Pattern> i$ = PA_LIST.iterator();
      while (i$.hasNext())
      {
        Pattern p = i$.next();
        String repl = (String)REPL_MAP.get(p.pattern());
        m = p.matcher(block.toString());
        if ((m.find()) && (t++ < 1000)) {
          s3 = m.start();
          e3 = m.end();
          low.replace(s1 + s3, s1 + e3, repl);
          src.replace(s1 + s3, s1 + e3, repl);
          block.replace(s3, e3, repl);
          index -= e3 - s3 - repl.length();
        }
      }
    } catch (Exception e) {
      logger.error("clearBlackList", e);
    }
    return index;
  }

  private static void clearOnEvent(StringBuilder low, StringBuilder src, StringBuilder block, int start)
  {
    try {
      Matcher m = EVENT_P.matcher(block.toString());
      int s3 = 0; int e3 = 0; int ic = 0;
      while (m.find()) {
        int k = m.group().indexOf("on");
        String replace = new StringBuilder().append(m.group().substring(0, k)).append("on_").append(m.group().substring(k + 2)).toString();

        s3 = m.start() + ic;
        e3 = m.end() + ic;
        low.replace(start + s3, start + e3, replace);
        src.replace(start + s3, start + e3, replace);
        start += replace.length() - m.group().length();
      }
    } catch (Exception e) {
      logger.error("clearOnEvent", e);
    }
  }

  private static void clearBlockUp(StringBuilder low, StringBuilder src, StringBuilder block, int start)
  {
    Matcher matcher = BROKE_UP_P.matcher(block.toString());
    int s3 = 0; int e3 = 0; int ic = 0;
    while (matcher.find()) {
      s3 = matcher.start();
      e3 = matcher.end();
      low.replace(start + s3 - ic, start + e3 - ic - (e3 - s3 - 1), "");
      src.replace(start + s3 - ic, start + e3 - ic - (e3 - s3 - 1), "");
      block.replace(s3 - ic, e3 - ic - 1, "");
      ic++;
    }
  }

  public static String antiMetaXSS(String content)
  {
    if (StringUtils.isNotEmpty(content)) {
      content = content.replaceAll("[\\x00-\\x08\\x0b-\\x0c\\x0e-\\x19\\xa0 ᠎᠏ -​    　﻿]", "");
      StringBuilder src = new StringBuilder(content);
      try {
        _antiXSS(content, src);
      } catch (Exception e) {
        logger.error("XSS_anti_error", e);
      }
      return src.toString().replaceAll("<\\s*meta\\s+http-equiv=.?refresh.+?>", "");
    }

    return content;
  }

  static
  {
    Map<String,String> replaceKV = new HashMap<String,String>();
    replaceKV.put("allowscriptaccess", "allow_script_access");
    replaceKV.put("javascript", "java_script");
    replaceKV.put("vbscript", "vb_script");
    replaceKV.put("livescript", "l_ivescript");
    replaceKV.put("-o-link", "_o_link");
    replaceKV.put("-moz-binding", "_moz_binding");
    replaceKV.put("@import", "_import");
    replaceKV.put("ms-its:", "ms_its_");
    replaceKV.put("mhtml:", "mhtml_");
    replaceKV.put("data:", "data_");
    replaceKV.put("firefoxurl:", "firefoxurl_");
    replaceKV.put("mocha:", "macha_");
    replaceKV.put("document.cookie", "document_cookie");
    replaceKV.put("behavior:url", "bahavior_url");
    replaceKV.put("autofocus", "au_tofocus");
    replaceKV.put("poster", "_po_ster");
    replaceKV.put("formaction", "fo_rmaction");
    replaceKV.put("background:url", "background_url");
    replaceKV.put("background-image:url", "backgroundimage-url");
    replaceKV.put("refresh", "re_fresh");
    replaceKV.put("fscommand", "fsc_ommand");
    replaceKV.put("seeksegmenttime", "seek_segment_time");

    StringBuilder rb = new StringBuilder(100);
    for (Map.Entry<String,String> kv : replaceKV.entrySet()) {
      if (kv != null)
      {
        for (int p = 0; p < ((String)kv.getKey()).length(); p++) {
          if (rb.length() > 0) {
            rb.append("\\s*");
          }
          rb.append(((String)kv.getKey()).charAt(p));
        }
        String regex = rb.toString();
        PA_LIST.add(Pattern.compile(regex));
        REPL_MAP.put(regex, kv.getValue().toString());
        rb.delete(0, rb.length());
      }
    }
    PA_LIST.add(Pattern.compile("&\\{"));
    REPL_MAP.put("&\\{", "__");
    PA_LIST.add(Pattern.compile("&amp;\\{"));
    REPL_MAP.put("&amp;\\{", "__");

    PA_LIST.add(Pattern.compile("[eｅ]\\s*[xｘ]\\s*[pｐ]\\s*[ｒr]\\s*[eｅ]\\s*[sｓ]\\s*[sｓ]\\s*[iｉ]\\s*[oｏ]\\s*[nｎ]"));

    REPL_MAP.put("[eｅ]\\s*[xｘ]\\s*[pｐ]\\s*[ｒr]\\s*[eｅ]\\s*[sｓ]\\s*[sｓ]\\s*[iｉ]\\s*[oｏ]\\s*[nｎ]", "expresionx_");

    setTagRepl();
  }

  private static class Tag
  {
    private final String tagStart;
    private final String tagEnd;
    private final int tagStartLen;
    private final int tagEndLen;
    private final String tagRepl;

    public Tag(String tagStart, String tagEnd, boolean nobrace, String tagRepl)
    {
      this.tagStart = tagStart;
      this.tagEnd = tagEnd;
      this.tagStartLen = tagStart.length();
      if (nobrace)
        this.tagEndLen = (tagEnd.length() + 1);
      else {
        this.tagEndLen = tagEnd.length();
      }
      this.tagRepl = tagRepl;
    }
  }
}