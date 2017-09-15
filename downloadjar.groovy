import groovy.util.CliBuilder

def returnOpts(args) {
    def cli = new CliBuilder(usage: 'downloadjar.groovy -[hraups]')
    // Create the list of options.
    cli.with {
        h longOpt: 'help',              'Show usage information'
        r longOpt: 'repository',        'URL to JAR repository'
        a longOpt: 'artifact',          'Artifact name'
        u longOpt: 'user',              'Username for basic auth'
        p longOpt: 'password',          'Password for basic auth'
        s longOpt: 'serverGroupName',   'Server group name'
    }

    def opts = cli.parse(args)

    // Show usage text when -h or --help option is used.
    if (opts.h) {
        cli.usage()
        // Will output:
        // usage: downloadjar.groovy -[hraups]
        //  -r,--repository       URL to JAR repository
        //  -a,--artifact         Artifact name
        //  -h,--help             Show usage information
        //  -u,--user             Username for basic auth
        //  -p,--password         Password for basic auth
        //  -s,--serverGroupName  serverGroupName
        System.exit(0)
    }

    if (!(opts.r && opts.a && opts.u && opts.p)) {
        cli.usage()
        System.exit(0)
    }
    return opts
}

def downloadjar(args) {
  def options = returnOpts(args)

}
