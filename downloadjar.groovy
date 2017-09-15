#!/usr/bin/env groovy

import groovy.util.CliBuilder

def returnOpts(args) {
    def cli = new CliBuilder(usage: './downloadjar.groovy -[hraups]')
    // Create the list of options.
    cli.with {
        h longOpt: 'help', 'Show usage information'
        r longOpt: 'repository', required: true, args: 1, 'URL to JAR repository'
        a longOpt: 'artifact', required: true, args: 1, 'Artifact name'
        u longOpt: 'user', required: true, args: 1, 'Username for basic auth'
        p longOpt: 'password', required: true, args: 1, 'Password for basic auth'
        s longOpt: 'serverGroupName', args: 1, 'Server group name'
    }

    def opts = cli.parse(args)

    // Show usage text when -h or --help option is used.
    if (opts.h) {
        cli.usage()
        // Will output:
        // usage: ./downloadjar.groovy -[hraups]
        //  -r,--repository       URL to JAR repository
        //  -a,--artifact         Artifact name
        //  -h,--help             Show usage information
        //  -u,--user             Username for basic auth
        //  -p,--password         Password for basic auth
        //  -s,--serverGroupName  serverGroupName
        System.exit(0)
    }
    return opts
}

def downloadjar(args) {
  def options = returnOpts(args)

  for (option in ['a', 'r', 'u', 'p']) {
    println "${option}: ${options[option]}"
  }
}

downloadjar(args)
