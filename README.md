# Source Lines of Code Maven Plugin

This [Apache Maven](https://maven.apache.org/) plugin counts [source lines of code](https://en.wikipedia.org/wiki/Source_lines_of_code) in a given Maven project.

A well-known SLOC tool is [cloc](https://github.com/AlDanial/cloc) by *Al Danial*.

## Overview

The plugin is a standard Apache Maven [plugin](https://maven.apache.org/plugin-developers/index.html):

```
$ cd sloc-maven-plugin
$ mvn clean install
```

The plugin finds each pom.xml's source codes and reports for each source file:
- Package name
- File name
- Type: [src, test, integration-test]
- Blank lines
- JavaDoc lines
- Comment lines
- Code lines
- Total lines

The output is displayed in a formatted table, similar to [MySQL Shell](https://dev.mysql.com/doc/refman/8.0/en/selecting-all.html)'s table outputs.

By default the package names are trimmed down to later unique suffixes to prevent too much repetition.

It can be tested right away on its own source code:

```
$ mvn io.github.orhankupusoglu:sloc-maven-plugin:sloc
[INFO] Scanning for projects...
[INFO] Inspecting build with total of 1 modules...
[INFO] Installing Nexus Staging features:
[INFO]   ... total of 1 executions of maven-deploy-plugin replaced with nexus-staging-maven-plugin
Downloading from central: https://repo.maven.apache.org/maven2/io/github/orhankupusoglu/sloc-maven-plugin/maven-metadata.xml
Downloaded from central: https://repo.maven.apache.org/maven2/io/github/orhankupusoglu/sloc-maven-plugin/maven-metadata.xml (379 B at 713 B/s)
[INFO]
[INFO] -------------< io.github.orhankupusoglu:sloc-maven-plugin >-------------
[INFO] Building sloc-maven-plugin 1.0.3
[INFO]   from pom.xml
[INFO] ----------------------------[ maven-plugin ]----------------------------
[INFO]
[INFO] --- sloc:1.0.3:sloc (default-cli) @ sloc-maven-plugin ---
[INFO] SLOC - directory: /home/orhanku/Me/Dev/sloc-maven-plugin/src
+------------------+------------------+----------+----------+----------+----------+----------+----------+
| Package Name     | File Name        | Type     | Blank    | JavaDoc  | Comment  | Code     | Total    |
+------------------+------------------+----------+----------+----------+----------+----------+----------+
| engine           | Common.java      | src      |       41 |        3 |        4 |      125 |      173 |
| engine           | CommonTest.java  | test     |       15 |        0 |        2 |       62 |       79 |
| engine           | CountLines.java  | src      |       10 |        0 |        0 |       42 |       52 |
| engine           | CountSLOC.java   | src      |       34 |       12 |        1 |      185 |      232 |
+------------------+------------------+----------+----------+----------+----------+----------+----------+
| goal             | GoalSLOC.java    | src      |       10 |       33 |        0 |       41 |       84 |
+------------------+------------------+----------+----------+----------+----------+----------+----------+
| 2 package(s)     | 5 file(s)        | java     |      110 |       48 |        7 |      455 |      620 |
+------------------+------------------+----------+----------+----------+----------+----------+----------+

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  1.741 s
[INFO] Finished at: 2023-11-28T02:50:13+01:00
[INFO] ------------------------------------------------------------------------

## use untrimmed package names
$ mvn io.github.orhankupusoglu:sloc-maven-plugin:sloc -DtrimPkgNames=false
[INFO] Scanning for projects...
[INFO] Inspecting build with total of 1 modules...
[INFO] Installing Nexus Staging features:
[INFO]   ... total of 1 executions of maven-deploy-plugin replaced with nexus-staging-maven-plugin
[INFO]
[INFO] -------------< io.github.orhankupusoglu:sloc-maven-plugin >-------------
[INFO] Building sloc-maven-plugin 1.0.2
[INFO]   from pom.xml
[INFO] ----------------------------[ maven-plugin ]----------------------------
[INFO]
[INFO] --- sloc:1.0.2:sloc (default-cli) @ sloc-maven-plugin ---
[INFO] SLOC - directory: /home/orhanku/Me/Dev/sloc-maven-plugin/src
+------------------------------------------+------------------+----------+----------+----------+----------+----------+----------+
| Package Name                             | File Name        | Type     | Blank    | JavaDoc  | Comment  | Code     | Total    |
+------------------------------------------+------------------+----------+----------+----------+----------+----------+----------+
| kupusoglu.orhan.sloc_maven_plugin.engine | Common.java      | src      |       41 |        3 |        4 |      125 |      173 |
| kupusoglu.orhan.sloc_maven_plugin.engine | CommonTest.java  | test     |       15 |        0 |        2 |       62 |       79 |
| kupusoglu.orhan.sloc_maven_plugin.engine | CountLines.java  | src      |       10 |        0 |        0 |       42 |       52 |
| kupusoglu.orhan.sloc_maven_plugin.engine | CountSLOC.java   | src      |       34 |       12 |        1 |      185 |      232 |
+------------------------------------------+------------------+----------+----------+----------+----------+----------+----------+
| kupusoglu.orhan.sloc_maven_plugin.goal   | GoalSLOC.java    | src      |       10 |       33 |        0 |       41 |       84 |
+------------------------------------------+------------------+----------+----------+----------+----------+----------+----------+
| 2 package(s)                             | 5 file(s)        | java     |      110 |       48 |        7 |      455 |      620 |
+------------------------------------------+------------------+----------+----------+----------+----------+----------+----------+

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  1.023 s
[INFO] Finished at: 2023-11-28T02:05:28+01:00
[INFO] ------------------------------------------------------------------------
```

&nbsp;

## Parameters

Parameters for the **sloc** goal can be supplied with **-Dname=value**, for example:

```
$ mvn io.github.orhankupusoglu:sloc-maven-plugin:sloc -DfileExt=cpp
```

Detailed plugin documentation can be generated with [Maven Site Plugin](https://maven.apache.org/plugins/maven-site-plugin/)'s **mvn site** goal, please check the HTML pages at **target/site/index.html**.
For example: **Project Reports > Plugin Documentation > sloc:sloc**

### goal: sloc
| Parameter      | Default Value | Description                                                           |
| :------------- | ------------- | --------------------------------------------------------------------- |
| srcMain        | src           | start in this directory and check files recursively                   |
| fileExt        | java          | count SLOC of files with this extension                               |
| trimPkgNames   | true          | trim common prefixes of the package names to remove clutter           |
| display        | true          | write SLOC data to **stdout**                                         |
| save           | false         | write SLOC data to **sloc.txt** to base dir where **pom.xml** resides |

&nbsp;

## Sample Project

[Google Guice](https://en.wikipedia.org/wiki/Google_Guice) is a well-known Java project.

```
$ git clone https://github.com/google/guice.git

$ cd guice/

$ git status
On branch master
Your branch is up to date with 'origin/master'.

nothing to commit, working tree clean

$ git log --oneline -n 5
e23d3b436 (HEAD -> master, origin/master, origin/HEAD) Internal change.
87d262c4d Remove stale comment
cf435821d Move exports to public target
cd1921901 Internal change
1ab911ad2 Internal change

## install all dependencies
$ mvn clean install

$ mvn io.github.orhankupusoglu:sloc-maven-plugin:sloc
...
```

The second run will give a simpler output:

```
$ mvn io.github.orhankupusoglu:sloc-maven-plugin:sloc -Ddisplay=false -Dsave=true

$ git status
On branch master
Your branch is up to date with 'origin/master'.

Untracked files:
  (use "git add <file>..." to include in what will be committed)
	core/sloc.txt
	extensions/assistedinject/sloc.txt
	extensions/dagger-adapter/sloc.txt
	extensions/grapher/sloc.txt
	extensions/jmx/sloc.txt
	extensions/jndi/sloc.txt
	extensions/persist/sloc.txt
	extensions/servlet/sloc.txt
	extensions/spring/sloc.txt
	extensions/testlib/sloc.txt
	extensions/throwingproviders/sloc.txt

nothing added to commit but untracked files present (use "git add" to track)

## for example
$ cat core/sloc.txt
+----------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| Package Name         | File Name                                  | Type     | Blank    | JavaDoc  | Comment  | Code     | Total    |
+----------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| inject.binder        | AnnotatedBindingBuilder.java               | src      |        5 |        5 |       15 |        8 |       33 |
| inject.binder        | AnnotatedConstantBindingBuilder.java       | src      |        5 |        5 |       15 |        8 |       33 |
| inject.binder        | AnnotatedElementBuilder.java               | src      |        5 |        6 |       15 |        8 |       34 |
| inject.binder        | ConstantBindingBuilder.java                | src      |       13 |        5 |       15 |       25 |       58 |
| inject.binder        | LinkedBindingBuilder.java                  | src      |       14 |       31 |       15 |       26 |       86 |
| inject.binder        | ScopedBindingBuilder.java                  | src      |        6 |       10 |       15 |       10 |       41 |
| inject.binder        | package-info.java                          | src      |        1 |        0 |       15 |        2 |       18 |
+----------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| inject.internal.aop  | AbstractGlueGenerator.java                 | src      |       29 |       42 |       21 |      124 |      216 |
| inject.internal.aop  | AnonymousClassDefiner.java                 | src      |        6 |        5 |       16 |       20 |       47 |
| inject.internal.aop  | BytecodeTasks.java                         | src      |       15 |        5 |       16 |      166 |      202 |
| inject.internal.aop  | ChildClassDefiner.java                     | src      |       14 |        5 |       18 |       51 |       88 |
| inject.internal.aop  | ClassBuilding.java                         | src      |       41 |       10 |       39 |      214 |      304 |
| inject.internal.aop  | ClassDefiner.java                          | src      |        3 |        0 |       15 |        6 |       24 |
| inject.internal.aop  | ClassDefining.java                         | src      |       11 |        5 |       17 |       47 |       80 |
| inject.internal.aop  | Enhancer.java                              | src      |       69 |       53 |       29 |      272 |      423 |
| inject.internal.aop  | EnhancerBuilderImpl.java                   | src      |       17 |        5 |       15 |       72 |      109 |
| inject.internal.aop  | FastClass.java                             | src      |       37 |       45 |       25 |      140 |      247 |
| inject.internal.aop  | GeneratedClassDefiner.java                 | src      |        6 |        5 |       15 |       12 |       38 |
| inject.internal.aop  | GlueException.java                         | src      |        3 |        0 |       15 |        8 |       26 |
| inject.internal.aop  | HiddenClassDefiner.java                    | src      |        7 |        5 |       15 |       47 |       74 |
| inject.internal.aop  | ImmutableStringTrie.java                   | src      |       53 |       90 |       32 |      160 |      335 |
| inject.internal.aop  | MethodPartition.java                       | src      |       23 |       17 |       38 |      110 |      188 |
| inject.internal.aop  | UnsafeClassDefiner.java                    | src      |       33 |        5 |       20 |      158 |      216 |
| inject.internal.aop  | UnsafeGetter.java                          | src      |        4 |        0 |       17 |       19 |       40 |
+----------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| inject.internal.util | CallerFinder.java                          | src      |        2 |        4 |        0 |        5 |       11 |
| inject.internal.util | Classes.java                               | src      |       13 |        4 |       15 |       43 |       75 |
| inject.internal.util | ContinuousStopwatch.java                   | src      |        7 |       10 |       15 |       25 |       57 |
| inject.internal.util | LineNumbers.java                           | src      |       34 |       23 |       21 |      186 |      264 |
| inject.internal.util | NewThrowableFinder.java                    | src      |        2 |        0 |        0 |       16 |       18 |
| inject.internal.util | SourceProvider.java                        | src      |       16 |        9 |       15 |       61 |      101 |
| inject.internal.util | StackTraceElements.java                    | src      |       25 |        8 |       15 |      156 |      204 |
+----------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| inject.internal      | AbstractBindingBuilder.java                | src      |       20 |       15 |       16 |       91 |      142 |
| inject.internal      | AbstractBindingProcessor.java              | src      |       19 |       24 |       24 |      115 |      182 |
| inject.internal      | AbstractProcessor.java                     | src      |        8 |        8 |       15 |       34 |       65 |
| inject.internal      | Annotations.java                           | src      |       49 |       48 |       19 |      342 |      458 |
| inject.internal      | BindingAlreadySetError.java                | src      |        6 |        0 |        0 |       46 |       52 |
| inject.internal      | BindingBuilder.java                        | src      |       29 |        5 |       17 |      153 |      204 |
| inject.internal      | BindingImpl.java                           | src      |       20 |        4 |       15 |       88 |      127 |
| inject.internal      | BindingProcessor.java                      | src      |       22 |        6 |       24 |      221 |      273 |
| inject.internal      | BoundProviderFactory.java                  | src      |        9 |        0 |       15 |       58 |       82 |
| inject.internal      | BytecodeGen.java                           | src      |       19 |       62 |       16 |       82 |      179 |
| inject.internal      | ChildBindingAlreadySetError.java           | src      |        9 |        4 |        3 |       69 |       85 |
| inject.internal      | ConstantBindingBuilderImpl.java            | src      |       23 |        5 |       16 |       97 |      141 |
| inject.internal      | ConstantFactory.java                       | src      |        7 |        0 |       15 |       19 |       41 |
| inject.internal      | ConstructionContext.java                   | src      |       16 |        5 |       19 |       51 |       91 |
| inject.internal      | ConstructionProxy.java                     | src      |        7 |        9 |       15 |       17 |       48 |
| inject.internal      | ConstructionProxyFactory.java              | src      |        3 |        5 |       15 |        5 |       28 |
| inject.internal      | ConstructorBindingImpl.java                | src      |       39 |        5 |       23 |      236 |      303 |
| inject.internal      | ConstructorInjector.java                   | src      |       14 |       10 |       21 |       91 |      136 |
| inject.internal      | ConstructorInjectorStore.java              | src      |       13 |       14 |       15 |       60 |      102 |
| inject.internal      | CreationListener.java                      | src      |        3 |        0 |       15 |        6 |       24 |
| inject.internal      | CycleDetectingLock.java                    | src      |       24 |       99 |       37 |      169 |      329 |
| inject.internal      | DeclaredMembers.java                       | src      |        5 |       11 |       16 |       27 |       59 |
| inject.internal      | DefaultConstructionProxyFactory.java       | src      |       20 |        5 |       15 |      103 |      143 |
| inject.internal      | DeferredLookups.java                       | src      |        7 |        6 |       15 |       34 |       62 |
| inject.internal      | DelayedInitialize.java                     | src      |        3 |        6 |       15 |        5 |       29 |
| inject.internal      | DelegatingInvocationHandler.java           | src      |        8 |        0 |       19 |       37 |       64 |
| inject.internal      | DuplicateElementError.java                 | src      |       10 |        4 |        8 |      105 |      127 |
| inject.internal      | DuplicateMapKeyError.java                  | src      |        8 |        4 |        0 |       58 |       70 |
| inject.internal      | Element.java                               | src      |        9 |        7 |       15 |       16 |       47 |
| inject.internal      | EncounterImpl.java                         | src      |       26 |        0 |       16 |      104 |      146 |
| inject.internal      | ErrorFormatter.java                        | src      |        4 |        3 |        0 |       26 |       33 |
| inject.internal      | ErrorHandler.java                          | src      |        5 |        5 |       15 |        8 |       33 |
| inject.internal      | ErrorId.java                               | src      |        2 |        0 |        1 |       64 |       67 |
| inject.internal      | Errors.java                                | src      |       98 |       40 |       21 |      559 |      718 |
| inject.internal      | ErrorsException.java                       | src      |        5 |        7 |       17 |       10 |       39 |
| inject.internal      | ExposedBindingImpl.java                    | src      |       11 |        0 |       17 |       47 |       75 |
| inject.internal      | ExposedKeyFactory.java                     | src      |        8 |        4 |       19 |       28 |       59 |
| inject.internal      | ExposureBuilder.java                       | src      |       10 |        0 |       15 |       44 |       69 |
| inject.internal      | FactoryProxy.java                          | src      |        9 |        4 |       15 |       45 |       73 |
| inject.internal      | FailableCache.java                         | src      |       10 |        6 |       15 |       60 |       91 |
| inject.internal      | GenericErrorDetail.java                    | src      |        5 |        0 |        0 |       29 |       34 |
| inject.internal      | GuiceInternal.java                         | src      |        2 |        7 |        0 |        5 |       14 |
| inject.internal      | Indexer.java                               | src      |       25 |        9 |       15 |      141 |      190 |
| inject.internal      | Initializable.java                         | src      |        3 |        5 |       15 |        5 |       28 |
| inject.internal      | Initializables.java                        | src      |        4 |        0 |       15 |       17 |       36 |
| inject.internal      | Initializer.java                           | src      |       26 |       41 |       39 |      169 |      275 |
| inject.internal      | InjectionRequestProcessor.java             | src      |       17 |        7 |       27 |      108 |      159 |
| inject.internal      | InjectorBindingData.java                   | src      |       39 |       10 |       18 |      202 |      269 |
| inject.internal      | InjectorImpl.java                          | src      |      141 |       85 |      111 |      907 |     1244 |
| inject.internal      | InjectorJitBindingData.java                | src      |       17 |       28 |        3 |       58 |      106 |
| inject.internal      | InjectorOptionsProcessor.java              | src      |       11 |        5 |       15 |       57 |       88 |
| inject.internal      | InjectorShell.java                         | src      |       52 |       28 |       29 |      250 |      359 |
| inject.internal      | InstanceBindingImpl.java                   | src      |       17 |        0 |       16 |       88 |      121 |
| inject.internal      | InterceptorBindingProcessor.java           | src      |        6 |        6 |       15 |       16 |       43 |
| inject.internal      | InterceptorStackCallback.java              | src      |       16 |       11 |       15 |       81 |      123 |
| inject.internal      | InternalClassesToSkipSources.java          | src      |        5 |        0 |       15 |       14 |       34 |
| inject.internal      | InternalContext.java                       | src      |       15 |       20 |       15 |       52 |      102 |
| inject.internal      | InternalErrorDetail.java                   | src      |        7 |        4 |        1 |       49 |       61 |
| inject.internal      | InternalFactory.java                       | src      |        4 |       13 |       15 |        6 |       38 |
| inject.internal      | InternalFactoryToInitializableAdapter.java | src      |        9 |        6 |       15 |       37 |       67 |
| inject.internal      | InternalFactoryToProviderAdapter.java      | src      |        8 |        0 |       20 |       31 |       59 |
| inject.internal      | InternalFlags.java                         | src      |       24 |       53 |       15 |      120 |      212 |
| inject.internal      | InternalInjectorCreator.java               | src      |       55 |       34 |       25 |      242 |      356 |
| inject.internal      | InternalProviderInstanceBindingImpl.java   | src      |       16 |       39 |        4 |      136 |      195 |
| inject.internal      | InternalProvisionException.java            | src      |       23 |       38 |       24 |      169 |      254 |
| inject.internal      | KotlinSupport.java                         | src      |       14 |        4 |        1 |       59 |       78 |
| inject.internal      | KotlinSupportInterface.java                | src      |        8 |        7 |        0 |       21 |       36 |
| inject.internal      | LinkedBindingImpl.java                     | src      |       16 |        0 |       15 |       83 |      114 |
| inject.internal      | LinkedProviderBindingImpl.java             | src      |       19 |        0 |       15 |      117 |      151 |
| inject.internal      | ListenerBindingProcessor.java              | src      |        6 |        5 |       15 |       18 |       44 |
| inject.internal      | LookupProcessor.java                       | src      |        8 |        6 |       16 |       33 |       63 |
| inject.internal      | Lookups.java                               | src      |        5 |        6 |       15 |        9 |       35 |
| inject.internal      | MembersInjectorImpl.java                   | src      |       14 |        5 |       30 |      140 |      189 |
| inject.internal      | MembersInjectorStore.java                  | src      |       15 |       18 |       16 |       98 |      147 |
| inject.internal      | MessageProcessor.java                      | src      |        8 |        6 |       15 |       27 |       56 |
| inject.internal      | Messages.java                              | src      |       41 |       32 |       22 |      221 |      316 |
| inject.internal      | MethodAspect.java                          | src      |       11 |       12 |       15 |       40 |       78 |
| inject.internal      | MissingConstructorError.java               | src      |        8 |        0 |        1 |       73 |       82 |
| inject.internal      | MissingImplementationError.java            | src      |       11 |        0 |        2 |       70 |       83 |
| inject.internal      | MissingImplementationErrorHints.java       | src      |       14 |        4 |       17 |       94 |      129 |
| inject.internal      | ModuleAnnotatedMethodScannerProcessor.java | src      |        5 |        5 |       15 |       12 |       37 |
| inject.internal      | MoreTypes.java                             | src      |       92 |       42 |       39 |      422 |      595 |
| inject.internal      | Nullability.java                           | src      |        4 |       14 |       15 |       15 |       48 |
| inject.internal      | PackageNameCompressor.java                 | src      |       25 |       20 |       62 |      146 |      253 |
| inject.internal      | PrivateElementProcessor.java               | src      |        7 |        5 |       15 |       20 |       47 |
| inject.internal      | PrivateElementsImpl.java                   | src      |       26 |        0 |       20 |      100 |      146 |
| inject.internal      | ProcessedBindingData.java                  | src      |       10 |       16 |       15 |       33 |       74 |
| inject.internal      | ProvidedByInternalFactory.java             | src      |        9 |        5 |       15 |       64 |       93 |
| inject.internal      | ProviderInstanceBindingImpl.java           | src      |       18 |        0 |       15 |      111 |      144 |
| inject.internal      | ProviderInternalFactory.java               | src      |       10 |        9 |       18 |       56 |       93 |
| inject.internal      | ProviderMethod.java                        | src      |       32 |       22 |       25 |      208 |      287 |
| inject.internal      | ProviderMethodsModule.java                 | src      |       35 |       14 |       38 |      273 |      360 |
| inject.internal      | ProviderToInternalFactoryAdapter.java      | src      |        8 |        0 |       18 |       32 |       58 |
| inject.internal      | ProvidesMethodScanner.java                 | src      |       14 |        7 |       19 |      142 |      182 |
| inject.internal      | ProvisionListenerCallbackStore.java        | src      |       14 |       21 |       20 |       78 |      133 |
| inject.internal      | ProvisionListenerStackCallback.java        | src      |       16 |        5 |       20 |       99 |      140 |
| inject.internal      | ProxyFactory.java                          | src      |       32 |        6 |       19 |      144 |      201 |
| inject.internal      | RealElement.java                           | src      |       14 |        0 |       16 |       69 |       99 |
| inject.internal      | RealMapBinder.java                         | src      |      195 |      103 |       79 |     1005 |     1382 |
| inject.internal      | RealMultibinder.java                       | src      |       84 |       37 |       38 |      454 |      613 |
| inject.internal      | RealOptionalBinder.java                    | src      |      111 |       37 |       67 |      589 |      804 |
| inject.internal      | ScopeBindingProcessor.java                 | src      |       10 |        6 |       17 |       31 |       64 |
| inject.internal      | ScopeNotFoundError.java                    | src      |        8 |        0 |        0 |       39 |       47 |
| inject.internal      | Scoping.java                               | src      |       52 |       27 |       19 |      225 |      323 |
| inject.internal      | SingleFieldInjector.java                   | src      |        7 |        0 |       16 |       35 |       58 |
| inject.internal      | SingleMemberInjector.java                  | src      |        4 |        0 |       15 |        7 |       26 |
| inject.internal      | SingleMethodInjector.java                  | src      |       10 |        0 |       15 |       70 |       95 |
| inject.internal      | SingleParameterInjector.java               | src      |       13 |        0 |       17 |       35 |       65 |
| inject.internal      | SingletonScope.java                        | src      |       21 |      111 |       33 |      145 |      310 |
| inject.internal      | SourceFormatter.java                       | src      |       16 |        6 |        4 |      143 |      169 |
| inject.internal      | TypeConverterBindingProcessor.java         | src      |       22 |        6 |       16 |      159 |      203 |
| inject.internal      | UniqueAnnotations.java                     | src      |       13 |        4 |       15 |       45 |       77 |
| inject.internal      | UntargettedBindingImpl.java                | src      |       13 |        0 |       15 |       64 |       92 |
| inject.internal      | UntargettedBindingProcessor.java           | src      |        9 |        5 |       20 |       43 |       77 |
| inject.internal      | WeakKeySet.java                            | src      |       19 |       13 |       20 |       88 |      140 |
| inject.internal      | package-info.java                          | src      |        1 |        0 |       15 |        2 |       18 |
+----------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| inject.matcher       | AbstractMatcher.java                       | src      |        4 |        7 |       18 |       12 |       41 |
| inject.matcher       | Matcher.java                               | src      |        5 |       13 |       15 |       11 |       44 |
| inject.matcher       | Matchers.java                              | src      |       98 |       15 |       15 |      347 |      475 |
| inject.matcher       | package-info.java                          | src      |        1 |        0 |       15 |        2 |       18 |
+----------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| inject.multibindings | ClassMapKey.java                           | src      |        4 |        5 |       15 |       13 |       37 |
| inject.multibindings | MapBinder.java                             | src      |       17 |      113 |       17 |       64 |      211 |
| inject.multibindings | MapBinderBinding.java                      | src      |       11 |       83 |       15 |       20 |      129 |
| inject.multibindings | MapKey.java                                | src      |        4 |       29 |       15 |       12 |       60 |
| inject.multibindings | Multibinder.java                           | src      |       18 |      102 |       17 |       59 |      196 |
| inject.multibindings | MultibinderBinding.java                    | src      |        9 |       54 |       15 |       17 |       95 |
| inject.multibindings | MultibindingsScanner.java                  | src      |        6 |       22 |       15 |       32 |       75 |
| inject.multibindings | MultibindingsTargetVisitor.java            | src      |        6 |       15 |       15 |        9 |       45 |
| inject.multibindings | OptionalBinder.java                        | src      |       14 |      126 |       17 |       39 |      196 |
| inject.multibindings | OptionalBinderBinding.java                 | src      |        8 |       48 |       15 |       14 |       85 |
| inject.multibindings | ProvidesIntoMap.java                       | src      |        4 |       27 |       15 |       13 |       59 |
| inject.multibindings | ProvidesIntoOptional.java                  | src      |        6 |       20 |       15 |       23 |       64 |
| inject.multibindings | ProvidesIntoSet.java                       | src      |        4 |       20 |       15 |       13 |       52 |
| inject.multibindings | StringMapKey.java                          | src      |        4 |        5 |       15 |       13 |       37 |
| inject.multibindings | package-info.java                          | src      |        1 |        4 |       15 |        1 |       21 |
+----------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| inject.name          | Named.java                                 | src      |        4 |        5 |       15 |       12 |       36 |
| inject.name          | NamedImpl.java                             | src      |       13 |        0 |       16 |       36 |       65 |
| inject.name          | Names.java                                 | src      |        8 |        9 |       16 |       30 |       63 |
| inject.name          | package-info.java                          | src      |        1 |        0 |       15 |        2 |       18 |
+----------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| inject.spi           | BindingScopingVisitor.java                 | src      |        7 |       22 |       15 |       10 |       54 |
| inject.spi           | BindingSourceRestriction.java              | src      |       30 |       55 |       21 |      246 |      352 |
| inject.spi           | BindingTargetVisitor.java                  | src      |       11 |       45 |       15 |       12 |       83 |
| inject.spi           | ConstructorBinding.java                    | src      |        6 |       19 |       15 |       13 |       53 |
| inject.spi           | ConvertedConstantBinding.java              | src      |        7 |       16 |       15 |       13 |       51 |
| inject.spi           | DefaultBindingScopingVisitor.java          | src      |        8 |        9 |       15 |       25 |       57 |
| inject.spi           | DefaultBindingTargetVisitor.java           | src      |       13 |        9 |       16 |       45 |       83 |
| inject.spi           | DefaultElementVisitor.java                 | src      |       21 |        9 |       15 |       76 |      121 |
| inject.spi           | Dependency.java                            | src      |       14 |       23 |       15 |       73 |      125 |
| inject.spi           | DisableCircularProxiesOption.java          | src      |        8 |        6 |       15 |       21 |       50 |
| inject.spi           | Element.java                               | src      |        6 |       35 |       15 |        7 |       63 |
| inject.spi           | ElementSource.java                         | src      |       13 |       61 |       15 |       41 |      130 |
| inject.spi           | ElementVisitor.java                        | src      |       20 |       45 |       15 |       32 |      112 |
| inject.spi           | Elements.java                              | src      |       74 |       34 |       45 |      548 |      701 |
| inject.spi           | ErrorDetail.java                           | src      |       14 |       51 |        1 |       62 |      128 |
| inject.spi           | ExposedBinding.java                        | src      |        5 |        6 |       15 |       10 |       36 |
| inject.spi           | HasDependencies.java                       | src      |        4 |       14 |       15 |        5 |       38 |
| inject.spi           | InjectionListener.java                     | src      |        3 |       13 |       15 |        4 |       35 |
| inject.spi           | InjectionPoint.java                        | src      |       99 |      198 |       40 |      610 |      947 |
| inject.spi           | InjectionRequest.java                      | src      |       14 |       27 |       15 |       49 |      105 |
| inject.spi           | InstanceBinding.java                       | src      |        5 |       12 |       15 |        8 |       40 |
| inject.spi           | InterceptorBinding.java                    | src      |       11 |       16 |       15 |       50 |       92 |
| inject.spi           | LinkedKeyBinding.java                      | src      |        4 |       10 |       15 |        6 |       35 |
| inject.spi           | MembersInjectorLookup.java                 | src      |       17 |       38 |       15 |       71 |      141 |
| inject.spi           | Message.java                               | src      |       24 |       35 |       15 |      107 |      181 |
| inject.spi           | ModuleAnnotatedMethodScanner.java          | src      |        5 |       29 |       15 |       10 |       59 |
| inject.spi           | ModuleAnnotatedMethodScannerBinding.java   | src      |       10 |        6 |       15 |       36 |       67 |
| inject.spi           | ModuleSource.java                          | src      |       14 |       45 |       15 |       55 |      129 |
| inject.spi           | PrivateElements.java                       | src      |        7 |       21 |       15 |       13 |       56 |
| inject.spi           | ProviderBinding.java                       | src      |        4 |       12 |       15 |        7 |       38 |
| inject.spi           | ProviderInstanceBinding.java               | src      |        6 |       24 |       15 |       10 |       55 |
| inject.spi           | ProviderKeyBinding.java                    | src      |        4 |       12 |       15 |        6 |       37 |
| inject.spi           | ProviderLookup.java                        | src      |       19 |       25 |       17 |       90 |      151 |
| inject.spi           | ProviderWithDependencies.java              | src      |        3 |        6 |       15 |        3 |       27 |
| inject.spi           | ProviderWithExtensionVisitor.java          | src      |        4 |       35 |       15 |        7 |       61 |
| inject.spi           | ProvidesMethodBinding.java                 | src      |        7 |       13 |       15 |       14 |       49 |
| inject.spi           | ProvidesMethodTargetVisitor.java           | src      |        4 |       12 |       15 |        5 |       36 |
| inject.spi           | ProvisionListener.java                     | src      |        7 |       32 |       15 |       12 |       66 |
| inject.spi           | ProvisionListenerBinding.java              | src      |       10 |       10 |       15 |       38 |       73 |
| inject.spi           | RequireAtInjectOnConstructorsOption.java   | src      |        8 |        6 |       15 |       22 |       51 |
| inject.spi           | RequireExactBindingAnnotationsOption.java  | src      |        8 |        6 |       15 |       21 |       50 |
| inject.spi           | RequireExplicitBindingsOption.java         | src      |        8 |        6 |       15 |       21 |       50 |
| inject.spi           | ScopeBinding.java                          | src      |       11 |       12 |       15 |       43 |       81 |
| inject.spi           | StaticInjectionRequest.java                | src      |       12 |       23 |       15 |       42 |       92 |
| inject.spi           | Toolable.java                              | src      |        4 |       12 |       15 |       12 |       43 |
| inject.spi           | TypeConverter.java                         | src      |        4 |        6 |       15 |        6 |       31 |
| inject.spi           | TypeConverterBinding.java                  | src      |       11 |       12 |       15 |       45 |       83 |
| inject.spi           | TypeEncounter.java                         | src      |       13 |       68 |       15 |       21 |      117 |
| inject.spi           | TypeListener.java                          | src      |        4 |       22 |       15 |        5 |       46 |
| inject.spi           | TypeListenerBinding.java                   | src      |       10 |       11 |       15 |       35 |       71 |
| inject.spi           | UntargettedBinding.java                    | src      |        3 |        7 |       15 |        3 |       28 |
| inject.spi           | package-info.java                          | src      |        1 |        0 |       15 |        2 |       18 |
+----------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| inject.util          | Enhanced.java                              | src      |        5 |        9 |       15 |       13 |       42 |
| inject.util          | Modules.java                               | src      |       57 |       78 |       27 |      318 |      480 |
| inject.util          | Providers.java                             | src      |       26 |       29 |       16 |      105 |      176 |
| inject.util          | Types.java                                 | src      |       15 |       66 |       16 |       51 |      148 |
| inject.util          | package-info.java                          | src      |        1 |        0 |       15 |        2 |       18 |
+----------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| inject               | AbstractModule.java                        | src      |       32 |       78 |       15 |      118 |      243 |
| inject               | Binder.java                                | src      |       33 |      369 |       15 |       56 |      473 |
| inject               | Binding.java                               | src      |        7 |       53 |       15 |       11 |       86 |
| inject               | BindingAnnotation.java                     | src      |        4 |       14 |       15 |        8 |       41 |
| inject               | ConfigurationException.java                | src      |       11 |       14 |       15 |       37 |       77 |
| inject               | CreationException.java                     | src      |        9 |        6 |       15 |       24 |       54 |
| inject               | Exposed.java                               | src      |        4 |        7 |       15 |       10 |       36 |
| inject               | Guice.java                                 | src      |        8 |       48 |       15 |       18 |       89 |
| inject               | ImplementedBy.java                         | src      |        5 |        5 |       15 |       11 |       36 |
| inject               | Inject.java                                | src      |        5 |       28 |       15 |       16 |       64 |
| inject               | Injector.java                              | src      |       23 |      243 |       15 |       30 |      311 |
| inject               | Key.java                                   | src      |       80 |       99 |       26 |      314 |      519 |
| inject               | MembersInjector.java                       | src      |        3 |       19 |       15 |        4 |       41 |
| inject               | Module.java                                | src      |        3 |       19 |       15 |        5 |       42 |
| inject               | OutOfScopeException.java                   | src      |        5 |        7 |       15 |       12 |       39 |
| inject               | PrivateBinder.java                         | src      |        8 |       17 |       15 |       12 |       52 |
| inject               | PrivateModule.java                         | src      |       35 |       87 |       17 |      133 |      272 |
| inject               | ProvidedBy.java                            | src      |        5 |        5 |       15 |       11 |       36 |
| inject               | Provider.java                              | src      |        3 |       30 |       15 |        7 |       55 |
| inject               | Provides.java                              | src      |        4 |        7 |       15 |       12 |       38 |
| inject               | ProvisionException.java                    | src      |       11 |        7 |       15 |       31 |       64 |
| inject               | RestrictedBindingSource.java               | src      |        8 |       87 |        0 |       26 |      121 |
| inject               | Scope.java                                 | src      |        4 |       28 |       15 |        6 |       53 |
| inject               | ScopeAnnotation.java                       | src      |        4 |       14 |       15 |        8 |       41 |
| inject               | Scopes.java                                | src      |       23 |       43 |       15 |      121 |      202 |
| inject               | Singleton.java                             | src      |        4 |        6 |       15 |        9 |       34 |
| inject               | Stage.java                                 | src      |        5 |       15 |       15 |        7 |       42 |
| inject               | TypeLiteral.java                           | src      |       40 |       71 |       18 |      211 |      340 |
| inject               | package-info.java                          | src      |        1 |       22 |       15 |        1 |       39 |
+----------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| 10 package(s)        | 256 file(s)                                | java     |     4240 |     5590 |     4371 |    20210 |    34411 |
+----------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+

## delete sloc.txt files
$ git clean -n
Would remove core/sloc.txt
Would remove extensions/assistedinject/sloc.txt
Would remove extensions/dagger-adapter/sloc.txt
Would remove extensions/grapher/sloc.txt
Would remove extensions/jmx/sloc.txt
Would remove extensions/jndi/sloc.txt
Would remove extensions/persist/sloc.txt
Would remove extensions/servlet/sloc.txt
Would remove extensions/spring/sloc.txt
Would remove extensions/testlib/sloc.txt
Would remove extensions/throwingproviders/sloc.txt

$ git clean -f
Removing core/sloc.txt
Removing extensions/assistedinject/sloc.txt
Removing extensions/dagger-adapter/sloc.txt
Removing extensions/grapher/sloc.txt
Removing extensions/jmx/sloc.txt
Removing extensions/jndi/sloc.txt
Removing extensions/persist/sloc.txt
Removing extensions/servlet/sloc.txt
Removing extensions/spring/sloc.txt
Removing extensions/testlib/sloc.txt
Removing extensions/throwingproviders/sloc.txt

$ git status
On branch master
Your branch is up to date with 'origin/master'.

nothing to commit, working tree clean
```
