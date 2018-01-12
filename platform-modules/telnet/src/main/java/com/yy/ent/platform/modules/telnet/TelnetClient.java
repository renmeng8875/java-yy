package com.yy.ent.platform.modules.telnet;

import jline.ArgumentCompletor;
import jline.ClassNameCompletor;
import jline.Completor;
import jline.ConsoleReader;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class TelnetClient {

    private final static char R = '\r';
    private final static char N = '\n';
    private final static char SPACE = ' ';
    private final static byte[] NEW_LINE = {R, N};
    private final static List<String> QUIT = Arrays.asList(new String[]{"QUIT", "Q", "EXIT", "BYE"});
    private final static String LINE_SEPARATOR = System.getProperty("line.separator");
    private static String PS1 = "> ";
    private final static String CMD_KEYWORD = "help,exit,charset,invoke";

    public static void main(String args[]) {
        // System.setProperty("jline.WindowsTerminal.directConsole","false");
        Socket socket = null;
        InputStream is = null;
        OutputStream os = null;
        ConsoleReader reader = null;
        try {
            String host = "0";
            int port = 30000;
            if (args.length == 1) {
                port = Integer.parseInt(args[0]);
            } else {
                System.err.println("usage: com.yy.ent.platform.modules.telnet.TelnetClient port");
            }

            socket = new Socket(host, port);
            is = socket.getInputStream();
            os = socket.getOutputStream();
            final InputStream is2 = is;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        reply(is2);
                    } catch (Exception e) {
                        System.err.println(e.toString());
                    }
                }
            }).start();
            reader = new ConsoleReader();
            List<Completor> completors = new ArrayList<Completor>();
            completors.add(new SimpleCompletor(CMD_KEYWORD.toUpperCase().split(",")));
            completors.add(new ClassNameCompletor());
            reader.addCompletor(new ArgumentCompletor(completors));
            String cmd = null;

            while ((cmd = reader.readLine(PS1)) != null) {
                cmd = cmd.trim();
                if (cmd.length() == 0) {
                    continue;
                }
                if (QUIT.contains(cmd.toUpperCase())) {
                    break;
                }
                if (!request(os, cmd)) {
                    System.out.println("Invalid argument(s)");
                }
            }

        } catch (Exception e) {
            System.err.println(e.toString());
        } finally {
            close(is);
            close(os);
            close(socket);
        }

    }

    private static boolean request(OutputStream os, String cmd) throws IOException {
        out(os, cmd.getBytes());
        return true;
    }

    private static void out(OutputStream os, byte[] bytes) throws IOException {
        os.write(bytes);
        os.write(NEW_LINE);
    }

    private static void close(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }

    /**
     * 回复
     *
     * @param inputStream
     * @throws Exception
     */
    private static void reply(InputStream inputStream) throws Exception {
        InputStreamReader reader = new InputStreamReader(inputStream);
        BufferedReader breader = new BufferedReader(reader);
        String line = null;
        while ((line = breader.readLine()) != null) {
            System.out.println(line);
            System.out.print(PS1);
        }
    }

}

/*
 * Copyright (c) 2002-2007, Marc Prud'hommeaux. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */

/**
 * <p>
 * A simple {@link Completor} implementation that handles a pre-defined list of
 * completion words.
 * </p>
 * <p/>
 * <p>
 * Example usage:
 * </p>
 * <p/>
 * <pre>
 * myConsoleReader.addCompletor(new SimpleCompletor(new String[] { &quot;now&quot;, &quot;yesterday&quot;, &quot;tomorrow&quot; }));
 * </pre>
 *
 * @author <a href="mailto:mwp1@cornell.edu">Marc Prud'hommeaux</a>
 */
@SuppressWarnings({"unchecked", "rawtypes"})
class SimpleCompletor implements Completor, Cloneable {
    /**
     * The list of candidates that will be completed.
     */
    SortedSet candidates;

    /**
     * A delimiter to use to qualify completions.
     */
    String delimiter;
    final SimpleCompletorFilter filter;

    /**
     * Create a new SimpleCompletor with a single possible completion values.
     */
    public SimpleCompletor(final String candidateString) {
        this(new String[]{candidateString});
    }

    /**
     * Create a new SimpleCompletor with a list of possible completion values.
     */
    public SimpleCompletor(final String[] candidateStrings) {
        this(candidateStrings, null);
    }

    public SimpleCompletor(final String[] strings, final SimpleCompletorFilter filter) {
        this.filter = filter;
        setCandidateStrings(strings);
    }

    /**
     * Complete candidates using the contents of the specified Reader.
     */
    public SimpleCompletor(final Reader reader) throws IOException {
        this(getStrings(reader));
    }

    /**
     * Complete candidates using the whitespearated values in read from the
     * specified Reader.
     */
    public SimpleCompletor(final InputStream in) throws IOException {
        this(getStrings(new InputStreamReader(in)));
    }

    private static String[] getStrings(final Reader in) throws IOException {
        final Reader reader = (in instanceof BufferedReader) ? in : new BufferedReader(in);

        List words = new LinkedList();
        String line;

        while ((line = ((BufferedReader) reader).readLine()) != null) {
            for (StringTokenizer tok = new StringTokenizer(line); tok.hasMoreTokens(); words.add(tok.nextToken())) {
                ;
            }
        }

        return (String[]) words.toArray(new String[words.size()]);
    }

    public int complete(final String buffer, final int cursor, final List clist) {
        String start = (buffer == null) ? "" : buffer;
        start = start.toUpperCase();
        SortedSet matches = candidates.tailSet(start);

        for (Iterator i = matches.iterator(); i.hasNext(); ) {
            String can = (String) i.next();

            if (!(can.startsWith(start))) {
                break;
            }

            if (delimiter != null) {
                int index = can.indexOf(delimiter, cursor);

                if (index != -1) {
                    can = can.substring(0, index + 1);
                }
            }

            clist.add(can);
        }

        if (clist.size() == 1) {
            clist.set(0, ((String) clist.get(0)) + " ");
        }

        // the index of the completion is always from the beginning of
        // the buffer.
        return (clist.size() == 0) ? (-1) : 0;
    }

    public void setDelimiter(final String delimiter) {
        this.delimiter = delimiter;
    }

    public String getDelimiter() {
        return this.delimiter;
    }

    public void setCandidates(final SortedSet candidates) {
        if (filter != null) {
            TreeSet filtered = new TreeSet();

            for (Iterator i = candidates.iterator(); i.hasNext(); ) {
                String element = (String) i.next();
                element = filter.filter(element);

                if (element != null) {
                    filtered.add(element);
                }
            }

            this.candidates = filtered;
        } else {
            this.candidates = candidates;
        }
    }

    public SortedSet getCandidates() {
        return Collections.unmodifiableSortedSet(this.candidates);
    }

    public void setCandidateStrings(final String[] strings) {
        setCandidates(new TreeSet(Arrays.asList(strings)));
    }

    public void addCandidateString(final String candidateString) {
        final String string = (filter == null) ? candidateString : filter.filter(candidateString);

        if (string != null) {
            candidates.add(string);
        }
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Filter for elements in the completor.
     *
     * @author <a href="mailto:mwp1@cornell.edu">Marc Prud'hommeaux</a>
     */
    public static interface SimpleCompletorFilter {
        /**
         * Filter the specified String. To not filter it, return the same String
         * as the parameter. To exclude it, return null.
         */
        public String filter(String element);
    }

    public static class NoOpFilter implements SimpleCompletorFilter {
        public String filter(final String element) {
            return element;
        }
    }
}
