NSIS Ant Task
===============

Description
---------------

Ant task for compiling [NSIS](http://nsis.sourceforge.net/) scripts on Windows, Linux and Mac systems. Help automate the build process of projects that use the NSIS installer.

To use it, put the NSIS Ant jarfile in an accessible location and add the following to your Ant script: 

    <taskdef name="nsis" classname="com.danielreese.nsisant.Task">
        <classpath location="nsisant-{version}.jar">
    </taskdef>

For more information, please view the [API](http://danielreese.com/nsis-ant/api/), visit the [project homepage](http://github.com/dreese/nsis-ant) or read the [MakeNSIS documentation](http://nsis.sourceforge.net/Docs/Chapter3.html).

Download
---------------

Download [NSIS Ant 1.3](http://github.com/downloads/dreese/nsis-ant/nsisant-1.3.jar) or the [source and Javadoc](http://github.com/downloads/dreese/nsis-ant/nsisant-1.3.zip).

NSIS Element Parameters
---------------

* script (required) &mdash; Path of the NSIS script to compile.
* verbosity &mdash; How much logging is written to standard output. Default is 0.
    <ol start="0">
        <li>no output</li>
        <li>errors only</li>
        <li>errors and warnings</li>
        <li>errors, warnings and info</li>
        <li>all output</li>
    </ol>
* out &mdash; File to which to log the output. Default is standard out.
* pause &mdash; Whether to pause after execution of the script. Default is no.
* noconfig &mdash; Whether to disable inclusion of nsisconf.nsh. Default is no.
* nocd &mdash; Whether to disable changing to the directory containing the script. Default is no.
* prefix &mdash; The prefix to use for the command-line options to <code>makensis</code>. Default is to use '/' on Windows and '-' otherwise.
* path &mdash; The directory containing the <code>makensis</code> executable. Default is to search the <code>PATH</code>.

Nested Elements and Parameters
---------------

**define**

Defines a symbol for the script. Equivalent to the /D (-D on non-Windows platforms) command line parameter of `makensis`. Multiple instances of this element are permitted. It supports the following parameters: 

* name (required) &mdash; The name of the symbol to define.
* value (required) &mdash; The value of the symbol.

**scriptcmd**

Executes an NSIS command in the script. Equivalent to the /X (-X on non-Windows platforms) command line parameter of `makensis`. Multiple instances of this element are allowed and are executed in the order specified. It supports the following parameters: 

* cmd (required)  &mdash; The NSIS command to execute in the script.

Examples
---------------

Compile a script using all of the default values.

    <nsis script="myproject.nsi"/>

Compile a script with verbose output, logging to a file, and the inclusion of `nsisconf.nsh` disabled. The VERSION symbol is set to the value "2.1" and the "AutoCloseWindow true" command is executed at the beginning of the script. 

    <nsis script="myproject.nsi" verbosity="4" out="build.log" noconfig="yes">
        <define name="VERSION" value="2.1"/>
        <scriptcmd cmd="AutoCloseWindow true"/>
    </nsis>

License
---------------

Copyright &copy; 2004 Wayne Grant. Relicensed with permission.  
Copyright &copy; 2005-2010 [Daniel Reese](http://www.danandcheryl.com/)

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.

