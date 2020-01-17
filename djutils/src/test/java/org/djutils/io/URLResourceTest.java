package org.djutils.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import org.junit.Assert;
import org.junit.Test;

/**
 * URLResourceTest.java.
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class URLResourceTest
{

    /**
     * Test whether URLResource retrieves files.
     * @throws IOException on I/O error
     */
    @Test
    public final void fileTest() throws IOException
    {
        // create a temporary file.
        File tempFile = File.createTempFile("filetest-", ".temp");
        String tempFilePath = tempFile.getAbsolutePath();
        URL url1 = URLResource.getResource(tempFilePath);
        Assert.assertNotNull(url1);
        Assert.assertEquals(new File(url1.getPath()).getAbsolutePath().replaceAll("\\\\", "/"),
                tempFilePath.replaceAll("\\\\", "/"));

        URL url2 = URLResource.getResource("/" + tempFilePath);
        Assert.assertNotNull(url2);
        Assert.assertEquals(new File(url2.getPath()).getAbsolutePath().replaceAll("\\\\", "/"),
                tempFilePath.replaceAll("\\\\", "/"));
    }

    /**
     * Test whether URLResource retrieves files.
     * @throws IOException on I/O error
     * @throws URISyntaxException on error
     */
    @Test
    public final void jarTest() throws IOException, URISyntaxException
    {
        // create a temporary jar file.
        File jarFile = File.createTempFile("filetest-", ".jar");
        File file1 = File.createTempFile("filetest-", ".f1");
        File file2 = File.createTempFile("filetest-", ".f2");
        File file3 = File.createTempFile("filetest-", ".f3");

        FileOutputStream fos = new FileOutputStream(jarFile.getAbsolutePath());
        JarOutputStream jos = new JarOutputStream(fos);
        addToJarFile(file1.getAbsolutePath(), jos);
        addToJarFile(file2.getAbsolutePath(), jos);
        addToJarFile(file3.getAbsolutePath(), jos);
        jos.close();
        fos.close();

        String jarFilePath = jarFile.getAbsolutePath();
        URL jarURL = URLResource.getResource(jarFilePath);
        Assert.assertNotNull(jarURL);
        // System.out.println(jarFile.getAbsolutePath() + "!" + file1.getName());
        // URL jar1 = URLResource.getResource(jarFile.getAbsolutePath() + "!" + file1.getName());
        // System.out.println(jar1.toURI());
        // Assert.assertNotNull(jar1);
    }

    /**
     * Copy a plain file into a jar file. 
     * @param fileName String; name of the file to copy
     * @param jos JarOutputStream; stream for writing into the jar file
     * @throws FileNotFoundException when the input file could not be found
     * @throws IOException when the input file could not be read, or writing to the jar stream fails
     */
    public void addToJarFile(final String fileName, final JarOutputStream jos) throws FileNotFoundException, IOException
    {
        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        JarEntry zipEntry = new JarEntry(file.getName());
        jos.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0)
        {
            jos.write(bytes, 0, length);
        }
        jos.closeEntry();
        fis.close();
    }

}
