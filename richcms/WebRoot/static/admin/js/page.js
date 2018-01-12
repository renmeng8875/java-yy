//container 容器，count 总页数 pageindex 当前页数
function setPage(container, count, pageindex,url) {
var container = container;
var pageurl = url;
var count = count;
var pageindex = pageindex;
var a = [];
  // 总页数少于10 全部显示,大于10 显示前3 后3 中间3 其余....
  if (pageindex == 1) {
    a[a.length] = "<a href='"+pageurl+"&pageno="+pageindex+"' class=\"prev unclick\">上一页</a>";
  } else {
    a[a.length] = "<a href='"+pageurl+"&pageno="+pageindex+"' class=\"prev\">上一页</a>";
  }
  function setPageList() {
    if (pageindex == i) {
      a[a.length] = "<a href='"+pageurl+"&pageno="+pageindex+"' class=\"on\">" + i + "</a>";
    } else {
      a[a.length] = "<a href='"+pageurl+"&pageno="+pageindex+"'>" + i + "</a>";
    }
  }
  // 总页数小于10
  if (count <= 10) {
    for (var i = 1; i <= count; i++) {
      setPageList();
    }
  }
  // 总页数大于10页
  else {
    if (pageindex <= 4) {
      for (var i = 1; i <= 5; i++) {
        setPageList();
      }
      a[a.length] = "...<a href='"+pageurl+"&pageno="+pageindex+"'>" + count + "</a>";
    } else if (pageindex >= count - 3) {
      a[a.length] = "<a href='"+pageurl+"&pageno="+pageindex+"'>1</a>...";
      for (var i = count - 4; i <= count; i++) {
        setPageList();
      }
    }
    else { // 当前页在中间部分
      a[a.length] = "<a href='"+pageurl+"&pageno="+pageindex+"'>1</a>...";
      for (var i = pageindex - 2; i <= pageindex + 2; i++) {
        setPageList();
      }
      a[a.length] = "...<a href='"+pageurl+"&pageno="+pageindex+"'>" + count + "</a>";
    }
  }
  if (pageindex == count) {
    a[a.length] = "<a href='"+pageurl+"&pageno="+pageindex+"' class=\"next unclick\">下一页</a>";
  } else {
    a[a.length] = "<a href='"+pageurl+"&pageno="+pageindex+"' class=\"next\">下一页</a>";
  }
  container.innerHTML = a.join("");
  // 事件点击
  var pageClick = function() {
    var oAlink = container.getElementsByTagName("a");
    var inx = pageindex; // 初始的页码
    oAlink[0].onclick = function() { // 点击上一页
      if (inx == 1) {
        return false;
      }
      inx--;
      window.location.href = pageurl+"&pageno="+inx;
      setPage(container, count, inx, pageurl);
      return false;
    }
    for (var i = 1; i < oAlink.length - 1; i++) { // 点击页码
      oAlink[i].onclick = function() {
        inx = parseInt(this.innerHTML);
        window.location.href = pageurl+"&pageno="+inx;
        setPage(container, count, inx, pageurl);
        return false;
      }
    }
    oAlink[oAlink.length - 1].onclick = function() { // 点击下一页
      if (inx == count) {
        return false;
      }
      inx++;
      window.location.href = pageurl+"&pageno="+inx;
      setPage(container, count, inx, pageurl);
      return false;
    }
  } ()
}

