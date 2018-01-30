### TL;DR : It's not a bug! It's a feature - http://bugs.sun.com/view_bug.do?bug_id=4761949

Following project demonstrates a problem (?) with classloading in TestNG 6.13.1.

Invoking `classLoader.getResource("org/testng")` returns `null`, but when using older version it returns URL to the jar file from classpath.

 * Use `mvn clean package` to run against TestNG 6.13.1 (test will fail)
 * Use `mvn clean package -Ptestng-old` to run against TestNG 5.x (test will pass)
 * Use `mvn clean package -Ptestng-patched` to run against locally stored TestNG 6.13.1 patched as described below (test will pass)

```
mvn -version
Apache Maven 3.5.0 (ff8f5e7444045639af65f6095c62210b5713f426; 2017-04-03T21:39:06+02:00)
Maven home: /usr/bin/mvn
Java version: 1.8.0_144, vendor: Oracle Corporation
Java home: /usr/java/jdk1.8.0_144/jre
Default locale: en_US, platform encoding: UTF-8
OS name: "linux", version: "4.14.14-200.fc26.x86_64", arch: "amd64", family: "unix"
```

The same behaviour under JDK6 from Oracle.

If you use repackaged JAR included in this repository (i.e. by using `<systemPath>` for dependency in Maven) this test will pass.


### Evaluation

I came across this behaviour while working on [Groovy 2.0 support for Spock Test Runner](https://issues.apache.org/jira/browse/GROOVY-6158). First I thought that there is some essential difference in the `MANIFEST` file. But after repacking (`jar xvf` and then `jar cvf`) including the same manifest file the test was passing. 

Interestingly enough those two JARs are identical in terms of the content (at least based on what `pkgdiff` is showing), but they were still different in the size. Then I tried `zipinfo`, and here's when I saw the real problem. Original JAR was packaged without the directory entries, and therefore, according to [this bug evaluation from Sun](http://bugs.sun.com/view_bug.do?bug_id=4761949) the JAR is not found through the `classLoader.getResource()` call. To quote:

> In general, Class.getResource() is intended to access file based resources (on the filesystem, or from jar files or wherever..) It is not specified
what the effect of accessing a directory entry is, and therefore this behavior can not be expected to be the same across different URL schemes.

