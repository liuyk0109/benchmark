package com.thelyk.benchmark;

import org.apache.commons.cli.*;

public class CmdParser {

    private static final Options OPTIONS = new Options();

    private static final String HELP_OPT = "h";
    private static final String HELP_LONG_OPT = "help";
    private static final String QPS_OPT = "q";
    private static final String QPS_LONG_OPT = "qps";
    private static final String SECONDS_OPT = "s";
    private static final String SECONDS_LONG_OPT = "seconds";
    private static final String METHOD_OPT = "m";
    private static final String METHOD_LONG_OPT = "method";
    private static final String URL_OPT = "u";
    private static final String URL_LONG_OPT = "url";

    static {
        OPTIONS.addOption(Option.builder(HELP_OPT).longOpt(HELP_LONG_OPT)
                .desc("usage help").build());
        OPTIONS.addOption(Option.builder(QPS_OPT).longOpt(QPS_LONG_OPT).hasArg().type(Long.class)
                .desc("queries per second").build());
        OPTIONS.addOption(Option.builder(SECONDS_OPT).longOpt(SECONDS_LONG_OPT).required().hasArg().type(Long.class)
                .desc("seconds to benchmark").build());
        OPTIONS.addOption(Option.builder(METHOD_OPT).longOpt(METHOD_LONG_OPT).hasArg().type(String.class)
                .desc("http request method, default GET").build());
        OPTIONS.addOption(Option.builder(URL_OPT).longOpt(URL_LONG_OPT).required().hasArg().type(String.class)
                .desc("http request url").build());
    }

    public static Args parse(String[] a) {
        Args args = new Args();
        HttpRequest httpRequest = new HttpRequest();
        args.setHttpRequest(httpRequest);
        CommandLineParser commandLineParser = new DefaultParser();
        CommandLine commandLine;
        try {
            commandLine = commandLineParser.parse(OPTIONS, a);
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            System.out.println(e.getMessage());
            formatter.printHelp("benchmark", OPTIONS);
            return null;
        }
        if (commandLine.hasOption(QPS_OPT)) {
            args.setQps(Long.parseLong(commandLine.getOptionValue(QPS_OPT)));
        }
        args.setTestSeconds(Long.parseLong(commandLine.getOptionValue(SECONDS_OPT)));
        if (commandLine.hasOption(METHOD_OPT)) {
            httpRequest.setMethod(commandLine.getOptionValue(METHOD_OPT));
        }
        httpRequest.setUrl(commandLine.getOptionValue(URL_OPT));
        return args;
    }
}
