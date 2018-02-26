package com.bartoszmajsak.groovy;

import java.net.URL;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ClassLoadingTest {

    @Test
    public void should_load_jcommander() {
        // when
        URL resource = this.getClass().getClassLoader().getResource("com/beust/jcommander");

        // then
        assertThat(resource).isNotNull();
    }

    @Test
    public void should_load_testng() {
        // when
        URL resource = this.getClass().getClassLoader().getResource("org/testng");

        // then
        assertThat(resource).isNotNull();
    }

    @Test
    public void should_load_junit() {
        // when
        URL resource = this.getClass().getClassLoader().getResource("junit");

        // then
        assertThat(resource).isNotNull();
    }

}
