/*
 * NSIS Ant Task
 * Copyright (c) 2004 Wayne Grant. Relicensed with permission.
 * Copyright (c) 2005-2010 Daniel Reese
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.danielreese.nsisant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.LogStreamHandler;
import org.apache.tools.ant.types.Commandline;

/**
 * This task allows an NSIS script to be compiled using Ant. It supports the
 * following attributes:
 * <ul>
 * <li>script</li>
 * <li>verbosity</li>
 * <li>out</li>
 * <li>pause</li>
 * <li>noconfig</li>
 * <li>nocd</li>
 * <li>prefix</li>
 * </ul>
 * The following nested elements are supported:
 * <ul>
 * <li>define</li>
 * <li>scriptcmd</li>
 * </ul>
 * 
 * @see <a href="http://nsis.sourceforge.net/Docs/Chapter3.html#3.1">NSIS
 *      Command Line Usage</a>
 * @see Define
 * @see Scriptcmd
 * @see Verbosity
 */
public class Task extends org.apache.tools.ant.Task
{
	// Constants
	/** NSIS command-line executable */
	protected static final String NSIS_CMD_LINE = "makensis" + (System.getProperty("os.name").startsWith("Windows") ? ".exe" : "");

	/** NSIS command-line option for verbosity */
	protected static final String NSIS_OPT_VERBOSITY = "V";

	/** NSIS command-line option for output file */
	protected static final String NSIS_OPT_OUTPUT = "O";

	/** NSIS command-line option for pause after script execution */
	protected static final String NSIS_OPT_PAUSE = "PAUSE";

	/** NSIS command-line option for no inclusion of config header */
	protected static final String NSIS_OPT_NOCONFIG = "NOCONFIG";

	/** NSIS command-line option for no change directory */
	protected static final String NSIS_OPT_NOCD = "NOCD";

	/** NSIS command-line option for a define */
	protected static final String NSIS_OPT_DEFINE = "D";

	/** NSIS command-line option for a script command */
	protected static final String NSIS_OPT_SCRIPTCMD = "X";

	// Fields
	/** Stores the value of the script attribute. */
	protected File m_fScript;

	/** Stores the value of the verbosity attribute. */
	protected Integer m_iVerbosity;

	/** Stores the value of the out attribute. */
	protected File m_fOut;

	/** Stores the value of the pause attribute. */
	protected Boolean m_bPause;

	/** Stores the value of the noconfig attribute. */
	protected Boolean m_bNoconfig;

	/** Stores the value of the nocd attribute. */
	protected Boolean m_bNocd;

	/** Stores the value of the makensis path */
	protected String m_sPath;

	/**
	 * Stores the value of the prefix attribute. Defaults to '/' on Windows and
	 * '-' on other platforms.
	 */
	protected char m_cPrefix = (System.getProperty("os.name").startsWith("Windows") ? '/' : '-');

	/** Stores nested define elements */
	protected List m_lDefines = new ArrayList();

	/** Stores nested execute elements */
	protected List m_lScriptcmds = new ArrayList();

	// Methods
	/** Task execution implementation. */
	public void execute()
	{
		// Check mandatory script attribute provided.
		if (m_fScript == null)
		{
			throw new BuildException("Attribute 'script' is required", getLocation());
		}

		// Check if path was specified.
		if (m_sPath == null)
		{
			m_sPath = "";
		}
		else if (!new File(m_sPath).exists())
		{
			throw new BuildException("Specified path does not exist: " + m_sPath, getLocation());
		}

		// Set up arguments to NSIS command-line
		List lArgs = new ArrayList();

		// Attributes
		if (m_iVerbosity != null)
		{
			lArgs.add(m_cPrefix + NSIS_OPT_VERBOSITY + m_iVerbosity);
		}

		if (m_fOut != null)
		{
			lArgs.add(m_cPrefix + NSIS_OPT_OUTPUT + m_fOut.getPath());
		}

		if (m_bPause != null)
		{
			lArgs.add(m_cPrefix + NSIS_OPT_PAUSE);
		}

		if (m_bNoconfig != null && m_bNoconfig.booleanValue())
		{
			lArgs.add(m_cPrefix + NSIS_OPT_NOCONFIG);
		}

		if (m_bNocd != null && m_bNocd.booleanValue())
		{
			lArgs.add(m_cPrefix + NSIS_OPT_NOCD);
		}

		// Nested define elements.
		for (int iCnt = 0; iCnt < m_lDefines.size(); iCnt++)
		{
			Define define = (Define) m_lDefines.get(iCnt);

			String sName = define.getName();
			if (sName == null)
			{
				throw new BuildException("Attribute 'name' is required", getLocation());
			}

			String sValue = define.getValue();
			if (sValue == null)
			{
				throw new BuildException("Attribute 'value' is required", getLocation());
			}

			lArgs.add(m_cPrefix + NSIS_OPT_DEFINE + sName + "=" + sValue);
		}

		// Nested scriptcmd elements.
		for (int iCnt = 0; iCnt < m_lScriptcmds.size(); iCnt++)
		{
			Scriptcmd scriptcmd = (Scriptcmd) m_lScriptcmds.get(iCnt);

			String sCmd = scriptcmd.getCmd();
			if (sCmd == null)
			{
				throw new BuildException("Attribute 'cmd' is required", getLocation());
			}

			lArgs.add(m_cPrefix + NSIS_OPT_SCRIPTCMD + sCmd);
		}

		// Script file.
		lArgs.add(m_fScript.getPath());

		// Create NSIS command-line
		Commandline cmd = new Commandline();
		cmd.setExecutable(m_sPath + NSIS_CMD_LINE);
		cmd.addArguments((String[]) lArgs.toArray(new String[lArgs.size()]));

		// Create Execute object from command-line
		Execute exe = new Execute(new LogStreamHandler(this, Project.MSG_INFO, Project.MSG_WARN));
		exe.setAntRun(getProject());
		exe.setWorkingDirectory(getProject().getBaseDir());
		exe.setCommandline(cmd.getCommandline());

		try
		{
			// Execute command
			int iError = exe.execute();
			if (iError != 0)
			{
				throw new BuildException("Command failed, error code " + iError + ": '" + cmd + "'", getLocation());
			}
			log("Successfully compiled script " + m_fScript, Project.MSG_INFO);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			throw new BuildException("Command failed: '" + cmd + "'", ex, getLocation());
		}
	}

	/**
	 * Handles nested <code>define</code> elements.
	 * 
	 * @param element the nested <code>define</code> element
	 */
	public void addDefine(Define element)
	{
		m_lDefines.add(element);
	}

	/**
	 * Handles nested <code>execute</code> elements.
	 * 
	 * @param element the nested <code>execute</code> element
	 */
	public void addScriptcmd(Scriptcmd element)
	{
		m_lScriptcmds.add(element);
	}

	/**
	 * Set the script attribute.
	 * 
	 * @param fScript Attribute value
	 */
	public void setScript(File fScript)
	{
		m_fScript = fScript;
	}

	/**
	 * Set the verbosity attribute.
	 * 
	 * @param verbosity the verbosity attribute
	 */
	public void setVerbosity(Verbosity verbosity)
	{
		try
		{
			m_iVerbosity = new Integer(verbosity.getValue());
		}
		catch (NumberFormatException ex)
		{
			// Should not happen.
			ex.printStackTrace();
		}
	}

	/**
	 * Set the out attribute.
	 * 
	 * @param fOut the out attribute
	 */
	public void setOut(File fOut)
	{
		m_fOut = fOut;
	}

	/**
	 * Set the pause attribute.
	 * 
	 * @param bPause the pause attribute
	 */
	public void setPause(Boolean bPause)
	{
		m_bPause = bPause;
	}

	/**
	 * Set the noconfig attribute.
	 * 
	 * @param bNoconfig the noconfig attribute
	 */
	public void setNoconfig(Boolean bNoconfig)
	{
		m_bNoconfig = bNoconfig;
	}

	/**
	 * Set the nocd attribute.
	 * 
	 * @param bNocd the nocd attribute
	 */
	public void setNocd(Boolean bNocd)
	{
		m_bNocd = bNocd;
	}

	/**
	 * Set the prefix attribute.
	 * 
	 * @param cPrefix the prefix attribute
	 */
	public void setPrefix(char cPrefix)
	{
		m_cPrefix = cPrefix;
	}

	/**
	 * Set the path to the makensis executable
	 * 
	 * @param path
	 */
	public void setPath(String path)
	{
		if (!path.endsWith("/"))
		{
			path += "/";
		}
		m_sPath = path;
	}
}
