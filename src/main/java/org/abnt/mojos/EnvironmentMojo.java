package org.abnt.mojos;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @goal env
 * @phase process-resources
 */
public class EnvironmentMojo extends AbstractMojo {

    public static final String ENVIRONMENTS_FOLDER = "environments";
    public static final String ENVIRONMENTS_FILE = "envm.environment";

    /**
     * Location of the build output.
     *
     * @parameter expression="${project.build.outputDirectory}"
     * @required
     */
    private File outputDirectory;

    /**
     * Environment to be used
     * @parameter expression="${envm.env}"
     */
    private String env;

    /**
     * Default environment to be used in case none is
     * passed (to be configured in pom)
     * @parameter
     */
    private String defaultEnv;

    /**
     * Project base folder
     * @parameter expression="${basedir}"
     */
    private File projectDirectory;

    public void execute()
            throws MojoExecutionException {
        File enviFile = new File(outputDirectory.getAbsolutePath() + File.separator + ENVIRONMENTS_FILE);
        if (enviFile.exists()) {
            enviFile.delete();
        }
        String theEnv = env !=null ? env : defaultEnv;
        if (theEnv!=null) {
            FileWriter out = null;
            try {
                try {
                    out = new FileWriter(enviFile);
                    out.write(theEnv);
                } finally {
                    if (out!=null) {
                        out.flush();
                        out.close();
                    }
                }
                final Log log = getLog();
                String envPath = projectDirectory.getAbsolutePath() + File.separator +
                        ENVIRONMENTS_FOLDER + File.separator + theEnv;
                File envRoot = new File(envPath);
                if (!envRoot.exists()) {
                    throw new MojoExecutionException("Environment folder not found : '" + envPath + "'. Make sure you have " +
                        "an 'environments/" + theEnv + "' folder in your project root.");
                } else {
                    if (!envRoot.isDirectory()) {
                        throw new MojoExecutionException("Environment ain't a folder : '" + envPath + "'.");
                    }
                    // cp -r everything in environments/myenv to target/classes
                    log.info("Copying resources from environment '" + theEnv + "' to '" + outputDirectory.getAbsolutePath() + "' :");
                    FolderCopy.copy(envRoot, outputDirectory, new FolderCopy.CopyCallback() {
                        @Override
                        public void onCopy(File fromDir, File toDir, File f) {
                            log.info("  " + f.getPath());
                        }
                    });
                }
            } catch (IOException e) {
                throw new MojoExecutionException("unable to copy environment file(s) for environment '" + theEnv + "'", e);
            }
        }
    }

}
