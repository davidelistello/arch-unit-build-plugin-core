package com.gb.cleanarch.inspector.core.lib.service;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JavaFileParserTest extends AbstractExcludePathTest
{
    private final JavaFileParser javaFileParser = new JavaFileParser();

    @BeforeClass
    public static void init() throws IOException
    {
        AbstractExcludePathTest.init();
    }

    @AfterClass
    public static void cleanup() throws IOException
    {
        AbstractExcludePathTest.cleanup();
    }

    @Test
    public void canParseJavaFile() throws IOException
    {
        final JavaFileParser.JavaFile fileWithPackage = javaFileParser.parse(getTempJavaFile(), getLogger());
        assertThat(fileWithPackage.getClassName()).isEqualTo(CLASS_NAME);
        assertThat(fileWithPackage.getPackageString()).isEqualTo(PACKAGE_NAME);
    }

    @Test
    public void canParseJavaFileWithoutPackage() throws IOException
    {
        final JavaFileParser.JavaFile fileWithoutPackage = javaFileParser.parse(getTempJavaFileWithDefaultPackage(), getLogger());
        assertThat(fileWithoutPackage.getClassName()).isEqualTo(CLASS_NAME);
        assertThat(fileWithoutPackage.getPackageString()).isNull();
    }

    @Test
    public void canParseJavaFileWithComments() throws IOException
    {
        final JavaFileParser.JavaFile fileWithFileComment = javaFileParser.parse(
                getTempJavaFileWithFileComment(), getLogger());
        assertThat(fileWithFileComment.getClassName()).isEqualTo(CLASS_NAME_WITH_FILE_COMMENT);
        assertThat(fileWithFileComment.getPackageString()).isEqualTo(PACKAGE_NAME);
    }


}
