package com.thelyk.benchmark;

import org.apache.commons.cli.*;

public class CmdParser {

    private static final Options OPTIONS = new Options();

    static {
        OPTIONS.addOption("help", "usage help");
        OPTIONS.addOption(Option.builder("q").longOpt("qps").hasArg().type(Long.class)
                .desc("queries per second").build());
        OPTIONS.addOption(Option.builder("s").longOpt("seconds").required().hasArg().type(Long.class)
                .desc("seconds to benchmark").build());
        OPTIONS.addOption(Option.builder("m").longOpt("method").hasArg().type(String.class)
                .desc("http request method, default GET").build());
        OPTIONS.addOption(Option.builder("u").longOpt("url").required().hasArg().type(String.class)
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
        if (commandLine.hasOption("q")) {
            args.setQps(Long.parseLong(commandLine.getOptionValue("q")));
        }
        args.setTestSeconds(Long.parseLong(commandLine.getOptionValue("s")));
        if (commandLine.hasOption("m")) {
            httpRequest.setMethod(commandLine.getOptionValue("q"));
        }
        httpRequest.setUrl(commandLine.getOptionValue("u"));
        return args;
    }
}
