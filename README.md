# Source Lines of Code Maven Plugin

This [Apache Maven](https://maven.apache.org/) plugin counts [source lines of code](https://en.wikipedia.org/wiki/Source_lines_of_code) in a given Maven project.

This plugin is available on the [Maven Central Repository](https://central.sonatype.com/artifact/io.github.orhankupusoglu/sloc-maven-plugin), and therefore can be called from the root directory of Java projects without explicit installation:
```
$ cd guava/
$ mvn io.github.orhankupusoglu:sloc-maven-plugin:sloc
```

A well-known SLOC tool is [cloc](https://github.com/AlDanial/cloc) by *Al Danial*.

## Overview

The plugin is a standard [Apache Maven plugin](https://maven.apache.org/plugin-developers/index.html), it requires Java 8 or newer and a recent Maven 3:

```
$ cd sloc-maven-plugin
$ mvn clean install
```

The plugin finds each pom.xml's source codes and reports for each source file:
- Package name
- File name
- Type: [src, test, int-test]
- Blank lines
- JavaDoc lines
- Comment lines
- Code lines
- Total lines

The output is displayed in a formatted table, similar to [MySQL Shell](https://dev.mysql.com/doc/refman/8.0/en/selecting-all.html)'s table outputs.

The package names can be shortened by removing the package name prefix shared by the most packages, in order to prevent too much repetition. A package that shares no prefix with the others is displayed in full. The feature is disabled by default and can be enabled with **-DtrimPkgNames=true**; the header of the first column then reads **Short Package Name** to mark the shortened names.

It can be tested right away on its own source code:

```
$ mvn io.github.orhankupusoglu:sloc-maven-plugin:sloc
[INFO] Scanning for projects...
[INFO] 
[INFO] -------------< io.github.orhankupusoglu:sloc-maven-plugin >-------------
[INFO] Building sloc-maven-plugin 1.1.0
[INFO]   from pom.xml
[INFO] ----------------------------[ maven-plugin ]----------------------------
[INFO] 
[INFO] --- sloc:1.1.0:sloc (default-cli) @ sloc-maven-plugin ---
[INFO] SLOC - directory: /home/orhanku/ME/Dev/sloc-maven-plugin/src
+------------------------------------------+--------------------+----------+----------+----------+----------+----------+----------+
| Package Name                             | File Name          | Type     | Blank    | JavaDoc  | Comment  | Code     | Total    |
+------------------------------------------+--------------------+----------+----------+----------+----------+----------+----------+
| foo.bar                                  | HelloWorld.java    | test     |        4 |        3 |       10 |        9 |       26 |
+------------------------------------------+--------------------+----------+----------+----------+----------+----------+----------+
| kupusoglu.orhan.sloc_maven_plugin.engine | Common.java        | src      |       53 |       13 |        8 |      160 |      234 |
| kupusoglu.orhan.sloc_maven_plugin.engine | CommonTest.java    | test     |       44 |        0 |        4 |      178 |      226 |
| kupusoglu.orhan.sloc_maven_plugin.engine | CountLines.java    | src      |       10 |        0 |        0 |       42 |       52 |
| kupusoglu.orhan.sloc_maven_plugin.engine | CountSLOC.java     | src      |       34 |       12 |        2 |      182 |      230 |
| kupusoglu.orhan.sloc_maven_plugin.engine | CountSLOCTest.java | test     |       18 |       20 |        2 |       71 |      111 |
+------------------------------------------+--------------------+----------+----------+----------+----------+----------+----------+
| kupusoglu.orhan.sloc_maven_plugin.goal   | GoalSLOC.java      | src      |       10 |       33 |        0 |       39 |       82 |
+------------------------------------------+--------------------+----------+----------+----------+----------+----------+----------+
| 3 package(s)                             | 7 file(s)          | java     |      173 |       81 |       26 |      681 |      961 |
+------------------------------------------+--------------------+----------+----------+----------+----------+----------+----------+

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  0.241 s
[INFO] Finished at: 2026-11-17T11:05:18+01:00
[INFO] ------------------------------------------------------------------------
```

Note: with the default settings the package names are printed in full. The run below shortens them: the standalone test fixture `foo.bar` shares no prefix with the other packages, therefore it is displayed in full, while the plugin's own packages are shortened to `engine` and `goal`.

```
## use short package names
$ mvn io.github.orhankupusoglu:sloc-maven-plugin:sloc -DtrimPkgNames=true
[INFO] Scanning for projects...
[INFO] 
[INFO] -------------< io.github.orhankupusoglu:sloc-maven-plugin >-------------
[INFO] Building sloc-maven-plugin 1.1.0
[INFO]   from pom.xml
[INFO] ----------------------------[ maven-plugin ]----------------------------
[INFO] 
[INFO] --- sloc:1.1.0:sloc (default-cli) @ sloc-maven-plugin ---
[INFO] SLOC - directory: /home/orhanku/ME/Dev/sloc-maven-plugin/src
+--------------------+--------------------+----------+----------+----------+----------+----------+----------+
| Short Package Name | File Name          | Type     | Blank    | JavaDoc  | Comment  | Code     | Total    |
+--------------------+--------------------+----------+----------+----------+----------+----------+----------+
| foo.bar            | HelloWorld.java    | test     |        4 |        3 |       10 |        9 |       26 |
+--------------------+--------------------+----------+----------+----------+----------+----------+----------+
| engine             | Common.java        | src      |       53 |       13 |        8 |      160 |      234 |
| engine             | CommonTest.java    | test     |       44 |        0 |        4 |      178 |      226 |
| engine             | CountLines.java    | src      |       10 |        0 |        0 |       42 |       52 |
| engine             | CountSLOC.java     | src      |       34 |       12 |        2 |      182 |      230 |
| engine             | CountSLOCTest.java | test     |       18 |       20 |        2 |       71 |      111 |
+--------------------+--------------------+----------+----------+----------+----------+----------+----------+
| goal               | GoalSLOC.java      | src      |       10 |       33 |        0 |       39 |       82 |
+--------------------+--------------------+----------+----------+----------+----------+----------+----------+
| 3 package(s)       | 7 file(s)          | java     |      173 |       81 |       26 |      681 |      961 |
+--------------------+--------------------+----------+----------+----------+----------+----------+----------+

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  0.206 s
[INFO] Finished at: 2026-11-17T11:05:19+01:00
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
| trimPkgNames   | false         | trim common prefixes of the package names to remove clutter           |
| display        | true          | write SLOC data to **stdout**                                         |
| save           | false         | write SLOC data to **sloc.txt** to base dir where **pom.xml** resides |

&nbsp;

## Sample Project

[Google Guava](https://github.com/google/guava) is a well-known Java project.

```
$ git clone https://github.com/google/guava.git

$ cd guava/

$ git status
On branch master
Your branch is up to date with 'origin/master'.

nothing to commit, working tree clean

$ git log --oneline -n 5
fce80fd984 Convert our FarmHash implementation to a streaming implementation.
022257016d Deflake `HashingTest.testGoodFastHash32`.
72a3f9060a Reformat with the new version of google-java-format.
d0eedd30a2 Add ranged `indexOf(array, target, fromIndex, toIndex)` overloads that search for a sub-array `target` within `[fromIndex, toIndex)` to `Bytes`, `Chars`, `Shorts`, `Ints`, `Longs`, `Floats`, `Doubles`, and `Booleans`, and delegate `indexOf(array, target)` to `indexOf(array, target, 0, array.length)`.
1b402e477e Deprecate `Ordering.arbitrary()`.

## display SLOC data
$ mvn io.github.orhankupusoglu:sloc-maven-plugin:sloc
...

## the second run will write the SLOC data into sloc.txt files, one per module
$ mvn io.github.orhankupusoglu:sloc-maven-plugin:sloc -Ddisplay=false -Dsave=true

$ git status
On branch master
Your branch is up to date with 'origin/master'.

Untracked files:
  (use "git add <file>..." to include in what will be committed)
	guava-gwt/sloc.txt
	guava-testlib/sloc.txt
	guava/sloc.txt

nothing added to commit but untracked files present (use "git add" to track)

## the guava module alone contains 19 packages and 611 files
$ cat guava/sloc.txt
+------------------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| Package Name                       | File Name                                  | Type     | Blank    | JavaDoc  | Comment  | Code     | Total    |
+------------------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
|                                    | module-info.java                           | src      |        2 |        1 |       15 |       24 |       42 |
+------------------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| com.google.common.annotations      | Beta.java                                  | src      |        3 |       13 |       13 |       17 |       46 |
| com.google.common.annotations      | GwtCompatible.java                         | src      |        5 |       25 |       13 |       14 |       57 |
| com.google.common.annotations      | GwtIncompatible.java                       | src      |        3 |       18 |       13 |       13 |       47 |
| com.google.common.annotations      | J2ktIncompatible.java                      | src      |        3 |        6 |       13 |        9 |       31 |
| com.google.common.annotations      | VisibleForTesting.java                     | src      |        2 |       13 |       13 |        4 |       32 |
| com.google.common.annotations      | package-info.java                          | src      |        1 |        4 |       13 |        1 |       19 |
+------------------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| com.google.common.base             | Absent.java                                | src      |       19 |        1 |       13 |       68 |      101 |
| com.google.common.base             | AbstractIterator.java                      | src      |       13 |        4 |       14 |       62 |       93 |
| com.google.common.base             | Ascii.java                                 | src      |       61 |      391 |       24 |      159 |      635 |
| com.google.common.base             | CaseFormat.java                            | src      |       29 |       27 |       15 |      150 |      221 |
| com.google.common.base             | CharMatcher.java                           | src      |      250 |      447 |       93 |     1046 |     1836 |
| com.google.common.base             | Charsets.java                              | src      |       10 |       43 |       13 |       20 |       86 |
| com.google.common.base             | CommonMatcher.java                         | src      |        8 |       17 |       13 |       11 |       49 |
| com.google.common.base             | CommonPattern.java                         | src      |        8 |        8 |       14 |       16 |       46 |
| com.google.common.base             | Converter.java                             | src      |       69 |      174 |      112 |      255 |      610 |
| com.google.common.base             | Defaults.java                              | src      |        6 |       11 |       18 |       39 |       74 |
| com.google.common.base             | Enums.java                                 | src      |       20 |       29 |       13 |       86 |      148 |
| com.google.common.base             | Equivalence.java                           | src      |       45 |      181 |       31 |      163 |      420 |
| com.google.common.base             | ExtraObjectsMethodsForWeb.java             | src      |        3 |        4 |       13 |        4 |       24 |
| com.google.common.base             | FinalizablePhantomReference.java           | src      |        3 |       17 |       13 |       15 |       48 |
| com.google.common.base             | FinalizableReference.java                  | src      |        3 |       12 |       13 |       10 |       38 |
| com.google.common.base             | FinalizableReferenceQueue.java             | src      |       34 |      143 |       76 |      186 |      439 |
| com.google.common.base             | FinalizableSoftReference.java              | src      |        3 |       14 |       13 |       15 |       45 |
| com.google.common.base             | FinalizableWeakReference.java              | src      |        3 |       14 |       13 |       15 |       45 |
| com.google.common.base             | Function.java                              | src      |        4 |       32 |       13 |       13 |       62 |
| com.google.common.base             | FunctionalEquivalence.java                 | src      |       12 |        6 |       13 |       46 |       77 |
| com.google.common.base             | Functions.java                             | src      |       61 |      116 |       18 |      255 |      450 |
| com.google.common.base             | IgnoreJRERequirement.java                  | src      |        4 |        5 |       13 |        8 |       30 |
| com.google.common.base             | Internal.java                              | src      |        5 |        8 |       16 |       18 |       47 |
| com.google.common.base             | Java8Compatibility.java                    | src      |        7 |        4 |       13 |       21 |       45 |
| com.google.common.base             | JdkPattern.java                            | src      |       17 |        1 |       13 |       60 |       91 |
| com.google.common.base             | Joiner.java                                | src      |       47 |      183 |       60 |      284 |      574 |
| com.google.common.base             | MoreObjects.java                           | src      |       38 |      218 |       16 |      206 |      478 |
| com.google.common.base             | NullnessCasts.java                         | src      |        4 |       31 |       13 |       12 |       60 |
| com.google.common.base             | Objects.java                               | src      |        6 |       47 |       13 |       16 |       82 |
| com.google.common.base             | Optional.java                              | src      |       24 |      251 |       13 |       75 |      363 |
| com.google.common.base             | PairwiseEquivalence.java                   | src      |       13 |        0 |       13 |       52 |       78 |
| com.google.common.base             | ParametricNullness.java                    | src      |        4 |       41 |       15 |       12 |       72 |
| com.google.common.base             | PatternCompiler.java                       | src      |        4 |       14 |       13 |       14 |       45 |
| com.google.common.base             | Platform.java                              | src      |       18 |       17 |       23 |       57 |      115 |
| com.google.common.base             | Preconditions.java                         | src      |       89 |      727 |       64 |      660 |     1540 |
| com.google.common.base             | Predicate.java                             | src      |        5 |       47 |       13 |       15 |       80 |
| com.google.common.base             | Predicates.java                            | src      |      102 |      230 |       28 |      445 |      805 |
| com.google.common.base             | Present.java                               | src      |       17 |        1 |       13 |       70 |      101 |
| com.google.common.base             | SmallCharMatcher.java                      | src      |       13 |       11 |       32 |       90 |      146 |
| com.google.common.base             | SneakyThrows.java                          | src      |        5 |       21 |       15 |       15 |       56 |
| com.google.common.base             | Splitter.java                              | src      |       55 |      275 |       31 |      257 |      618 |
| com.google.common.base             | StandardSystemProperty.java                | src      |       36 |       66 |       13 |       49 |      164 |
| com.google.common.base             | Stopwatch.java                             | src      |       22 |      124 |       14 |      126 |      286 |
| com.google.common.base             | Strings.java                               | src      |       24 |      125 |       19 |      151 |      319 |
| com.google.common.base             | Supplier.java                              | src      |        4 |       35 |       13 |       12 |       64 |
| com.google.common.base             | Suppliers.java                             | src      |       53 |       95 |       36 |      272 |      456 |
| com.google.common.base             | Throwables.java                            | src      |       35 |      238 |       40 |      244 |      557 |
| com.google.common.base             | Ticker.java                                | src      |        6 |       18 |       13 |       18 |       55 |
| com.google.common.base             | Utf8.java                                  | src      |       15 |       42 |       35 |      107 |      199 |
| com.google.common.base             | Verify.java                                | src      |       32 |      287 |       23 |      198 |      540 |
| com.google.common.base             | VerifyException.java                       | src      |        7 |       21 |       13 |       19 |       60 |
| com.google.common.base             | package-info.java                          | src      |        2 |       44 |       13 |        5 |       64 |
+------------------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| com.google.common.base.internal    | Finalizer.java                             | src      |       26 |       43 |       51 |      145 |      265 |
+------------------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| com.google.common.cache            | AbstractCache.java                         | src      |       31 |      105 |       18 |      137 |      291 |
| com.google.common.cache            | AbstractLoadingCache.java                  | src      |        8 |       15 |       13 |       42 |       78 |
| com.google.common.cache            | Cache.java                                 | src      |       15 |      119 |       18 |       33 |      185 |
| com.google.common.cache            | CacheBuilder.java                          | src      |       78 |      593 |       31 |      445 |     1147 |
| com.google.common.cache            | CacheBuilderSpec.java                      | src      |       46 |       85 |       15 |      353 |      499 |
| com.google.common.cache            | CacheLoader.java                           | src      |       22 |      121 |       17 |       91 |      251 |
| com.google.common.cache            | CacheStats.java                            | src      |       25 |      137 |       13 |      129 |      304 |
| com.google.common.cache            | ForwardingCache.java                       | src      |       20 |       34 |       18 |       76 |      148 |
| com.google.common.cache            | ForwardingLoadingCache.java                | src      |       13 |       19 |       13 |       49 |       94 |
| com.google.common.cache            | IgnoreJRERequirement.java                  | src      |        4 |        5 |       13 |        8 |       30 |
| com.google.common.cache            | LoadingCache.java                          | src      |        9 |      120 |       13 |       29 |      171 |
| com.google.common.cache            | LocalCache.java                            | src      |      718 |      347 |      277 |     3654 |     4996 |
| com.google.common.cache            | LongAddable.java                           | src      |        5 |        5 |       13 |        8 |       31 |
| com.google.common.cache            | LongAddables.java                          | src      |        5 |        5 |       13 |       11 |       34 |
| com.google.common.cache            | NullnessCasts.java                         | src      |        5 |       32 |       14 |       17 |       68 |
| com.google.common.cache            | ParametricNullness.java                    | src      |        4 |       41 |       15 |       12 |       72 |
| com.google.common.cache            | ReferenceEntry.java                        | src      |       21 |       37 |       23 |       30 |      111 |
| com.google.common.cache            | RemovalCause.java                          | src      |        8 |       34 |       13 |       39 |       94 |
| com.google.common.cache            | RemovalListener.java                       | src      |        3 |       22 |       15 |        7 |       47 |
| com.google.common.cache            | RemovalListeners.java                      | src      |        6 |       13 |       13 |       15 |       47 |
| com.google.common.cache            | RemovalNotification.java                   | src      |        9 |       23 |       13 |       27 |       72 |
| com.google.common.cache            | Weigher.java                               | src      |        4 |       12 |       13 |        7 |       36 |
| com.google.common.cache            | package-info.java                          | src      |        2 |       16 |       13 |        5 |       36 |
+------------------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| com.google.common.collect          | AbstractBiMap.java                         | src      |       74 |       21 |       46 |      352 |      493 |
| com.google.common.collect          | AbstractIndexedListIterator.java           | src      |       13 |       25 |       15 |       54 |      107 |
| com.google.common.collect          | AbstractIterator.java                      | src      |       16 |       78 |       19 |       65 |      178 |
| com.google.common.collect          | AbstractListMultimap.java                  | src      |       16 |       53 |       16 |       62 |      147 |
| com.google.common.collect          | AbstractMapBasedMultimap.java              | src      |      230 |      160 |       80 |     1208 |     1678 |
| com.google.common.collect          | AbstractMapBasedMultiset.java              | src      |       44 |       23 |       39 |      247 |      353 |
| com.google.common.collect          | AbstractMapEntry.java                      | src      |        9 |       10 |       15 |       41 |       75 |
| com.google.common.collect          | AbstractMultimap.java                      | src      |       30 |       19 |       18 |      146 |      213 |
| com.google.common.collect          | AbstractMultiset.java                      | src      |       32 |       36 |       20 |      116 |      204 |
| com.google.common.collect          | AbstractNavigableMap.java                  | src      |       30 |        5 |       26 |      125 |      186 |
| com.google.common.collect          | AbstractRangeSet.java                      | src      |       17 |        5 |       13 |       66 |      101 |
| com.google.common.collect          | AbstractSequentialIterator.java            | src      |        7 |       28 |       15 |       25 |       75 |
| com.google.common.collect          | AbstractSetMultimap.java                   | src      |       17 |       58 |       16 |       66 |      157 |
| com.google.common.collect          | AbstractSortedKeySortedSetMultimap.java    | src      |        8 |        8 |       15 |       29 |       60 |
| com.google.common.collect          | AbstractSortedMultiset.java                | src      |       20 |        9 |       15 |      102 |      146 |
| com.google.common.collect          | AbstractSortedSetMultimap.java             | src      |       15 |       57 |       16 |       65 |      153 |
| com.google.common.collect          | AbstractTable.java                         | src      |       36 |        6 |       13 |      183 |      238 |
| com.google.common.collect          | AllEqualOrdering.java                      | src      |       11 |        5 |       15 |       38 |       69 |
| com.google.common.collect          | ArrayListMultimap.java                     | src      |       16 |       70 |       16 |       77 |      179 |
| com.google.common.collect          | ArrayTable.java                            | src      |       92 |      228 |       38 |      445 |      803 |
| com.google.common.collect          | BaseImmutableMultimap.java                 | src      |        2 |        4 |       15 |        4 |       25 |
| com.google.common.collect          | BiMap.java                                 | src      |       10 |       70 |       18 |       19 |      117 |
| com.google.common.collect          | BoundType.java                             | src      |        6 |        9 |       13 |       14 |       42 |
| com.google.common.collect          | ByFunctionOrdering.java                    | src      |       10 |        4 |       15 |       43 |       72 |
| com.google.common.collect          | CartesianList.java                         | src      |       17 |        5 |       14 |      134 |      170 |
| com.google.common.collect          | ClassToInstanceMap.java                    | src      |        4 |       38 |       15 |       15 |       72 |
| com.google.common.collect          | CollectCollectors.java                     | src      |       45 |        1 |       33 |      380 |      459 |
| com.google.common.collect          | CollectPreconditions.java                  | src      |       11 |        6 |       15 |       46 |       78 |
| com.google.common.collect          | CollectSpliterators.java                   | src      |       47 |       55 |       23 |      426 |      551 |
| com.google.common.collect          | Collections2.java                          | src      |       74 |      173 |       33 |      428 |      708 |
| com.google.common.collect          | CompactHashMap.java                        | src      |      128 |      134 |      115 |      826 |     1203 |
| com.google.common.collect          | CompactHashSet.java                        | src      |       82 |      135 |       36 |      504 |      757 |
| com.google.common.collect          | CompactHashing.java                        | src      |       24 |       42 |       18 |      115 |      199 |
| com.google.common.collect          | CompactLinkedHashMap.java                  | src      |       46 |       41 |       32 |      202 |      321 |
| com.google.common.collect          | CompactLinkedHashSet.java                  | src      |       41 |       55 |       33 |      154 |      283 |
| com.google.common.collect          | ComparatorOrdering.java                    | src      |       10 |        1 |       15 |       40 |       66 |
| com.google.common.collect          | Comparators.java                           | src      |       16 |      145 |       19 |       94 |      274 |
| com.google.common.collect          | ComparisonChain.java                       | src      |       37 |      139 |       15 |      117 |      308 |
| com.google.common.collect          | CompoundOrdering.java                      | src      |       10 |        1 |       15 |       52 |       78 |
| com.google.common.collect          | ComputationException.java                  | src      |        4 |       14 |       15 |       13 |       46 |
| com.google.common.collect          | ConcurrentHashMultiset.java                | src      |       57 |      125 |       78 |      370 |      630 |
| com.google.common.collect          | ConsumingQueueIterator.java                | src      |        6 |        4 |       14 |       19 |       43 |
| com.google.common.collect          | ContiguousSet.java                         | src      |       28 |      103 |       22 |      124 |      277 |
| com.google.common.collect          | Count.java                                 | src      |       12 |        5 |       13 |       40 |       70 |
| com.google.common.collect          | Cut.java                                   | src      |       86 |        9 |       36 |      345 |      476 |
| com.google.common.collect          | DenseImmutableTable.java                   | src      |       51 |        2 |       26 |      252 |      331 |
| com.google.common.collect          | DescendingImmutableSortedMultiset.java     | src      |       15 |        5 |       14 |       58 |       92 |
| com.google.common.collect          | DescendingImmutableSortedSet.java          | src      |       20 |        5 |       16 |       84 |      125 |
| com.google.common.collect          | DescendingMultiset.java                    | src      |       23 |        6 |       15 |      105 |      149 |
| com.google.common.collect          | DiscreteDomain.java                        | src      |       48 |       95 |       15 |      182 |      340 |
| com.google.common.collect          | EmptyContiguousSet.java                    | src      |       30 |        5 |       13 |      134 |      182 |
| com.google.common.collect          | EmptyImmutableListMultimap.java            | src      |        7 |        5 |       21 |       20 |       53 |
| com.google.common.collect          | EmptyImmutableSetMultimap.java             | src      |        7 |        5 |       21 |       20 |       53 |
| com.google.common.collect          | EnumBiMap.java                             | src      |       16 |       31 |       29 |       86 |      162 |
| com.google.common.collect          | EnumHashBiMap.java                         | src      |       15 |       31 |       24 |       69 |      139 |
| com.google.common.collect          | EnumMultiset.java                          | src      |       30 |       34 |       18 |      239 |      321 |
| com.google.common.collect          | EvictingQueue.java                         | src      |       15 |       39 |       25 |       66 |      145 |
| com.google.common.collect          | ExplicitOrdering.java                      | src      |       12 |        1 |       15 |       46 |       74 |
| com.google.common.collect          | FilteredEntryMultimap.java                 | src      |       57 |        6 |       22 |      377 |      462 |
| com.google.common.collect          | FilteredEntrySetMultimap.java              | src      |       12 |        5 |       15 |       45 |       77 |
| com.google.common.collect          | FilteredKeyListMultimap.java               | src      |        7 |        5 |       15 |       28 |       55 |
| com.google.common.collect          | FilteredKeyMultimap.java                   | src      |       33 |        5 |       14 |      177 |      229 |
| com.google.common.collect          | FilteredKeySetMultimap.java                | src      |       12 |        5 |       15 |       45 |       77 |
| com.google.common.collect          | FilteredMultimap.java                      | src      |        4 |        5 |       15 |       11 |       35 |
| com.google.common.collect          | FilteredMultimapValues.java                | src      |       12 |        5 |       13 |       64 |       94 |
| com.google.common.collect          | FilteredSetMultimap.java                   | src      |        3 |        5 |       15 |        9 |       32 |
| com.google.common.collect          | FluentIterable.java                        | src      |       52 |      527 |       26 |      254 |      859 |
| com.google.common.collect          | ForwardingBlockingDeque.java               | src      |       20 |       24 |       15 |       76 |      135 |
| com.google.common.collect          | ForwardingCollection.java                  | src      |       33 |      107 |       16 |      128 |      284 |
| com.google.common.collect          | ForwardingConcurrentMap.java               | src      |       13 |       24 |       15 |       44 |       96 |
| com.google.common.collect          | ForwardingDeque.java                       | src      |       22 |       18 |       15 |       97 |      152 |
| com.google.common.collect          | ForwardingImmutableList.java               | src      |        3 |        5 |       15 |        6 |       29 |
| com.google.common.collect          | ForwardingImmutableMap.java                | src      |        3 |        5 |       15 |        6 |       29 |
| com.google.common.collect          | ForwardingImmutableSet.java                | src      |        3 |        5 |       15 |        6 |       29 |
| com.google.common.collect          | ForwardingIterator.java                    | src      |       12 |       24 |       15 |       38 |       89 |
| com.google.common.collect          | ForwardingList.java                        | src      |       31 |      104 |       16 |      118 |      269 |
| com.google.common.collect          | ForwardingListIterator.java                | src      |       11 |       16 |       15 |       38 |       80 |
| com.google.common.collect          | ForwardingListMultimap.java                | src      |        8 |       13 |       15 |       26 |       62 |
| com.google.common.collect          | ForwardingMap.java                         | src      |       36 |      133 |       16 |      145 |      330 |
| com.google.common.collect          | ForwardingMapEntry.java                    | src      |       17 |       53 |       16 |       67 |      153 |
| com.google.common.collect          | ForwardingMultimap.java                    | src      |       25 |       13 |       16 |      102 |      156 |
| com.google.common.collect          | ForwardingMultiset.java                    | src      |       36 |      148 |       15 |      136 |      335 |
| com.google.common.collect          | ForwardingNavigableMap.java                | src      |       54 |      147 |       15 |      223 |      439 |
| com.google.common.collect          | ForwardingNavigableSet.java                | src      |       33 |       96 |       15 |      124 |      268 |
| com.google.common.collect          | ForwardingObject.java                      | src      |        7 |       32 |       16 |       12 |       67 |
| com.google.common.collect          | ForwardingQueue.java                       | src      |       17 |       49 |       15 |       71 |      152 |
| com.google.common.collect          | ForwardingSet.java                         | src      |       14 |       51 |       16 |       44 |      125 |
| com.google.common.collect          | ForwardingSetMultimap.java                 | src      |        8 |       13 |       15 |       31 |       67 |
| com.google.common.collect          | ForwardingSortedMap.java                   | src      |       19 |       56 |       22 |       83 |      180 |
| com.google.common.collect          | ForwardingSortedMultiset.java              | src      |       26 |       92 |       13 |      124 |      255 |
| com.google.common.collect          | ForwardingSortedSet.java                   | src      |       18 |       57 |       21 |       87 |      183 |
| com.google.common.collect          | ForwardingSortedSetMultimap.java           | src      |        9 |       13 |       15 |       29 |       66 |
| com.google.common.collect          | ForwardingTable.java                       | src      |       25 |        9 |       15 |      102 |      151 |
| com.google.common.collect          | GeneralRange.java                          | src      |       35 |       27 |       24 |      233 |      319 |
| com.google.common.collect          | HashBasedTable.java                        | src      |       12 |       38 |       15 |       43 |      108 |
| com.google.common.collect          | HashBiMap.java                             | src      |      124 |       53 |       25 |      773 |      975 |
| com.google.common.collect          | HashMultimap.java                          | src      |       14 |       62 |       15 |       64 |      155 |
| com.google.common.collect          | HashMultiset.java                          | src      |       12 |       26 |       15 |       46 |       99 |
| com.google.common.collect          | Hashing.java                               | src      |       10 |        7 |       30 |       30 |       77 |
| com.google.common.collect          | IgnoreJRERequirement.java                  | src      |        4 |        5 |       13 |        8 |       30 |
| com.google.common.collect          | ImmutableAsList.java                       | src      |       13 |        8 |       17 |       52 |       90 |
| com.google.common.collect          | ImmutableBiMap.java                        | src      |       47 |      253 |       32 |      345 |      677 |
| com.google.common.collect          | ImmutableClassToInstanceMap.java           | src      |       22 |       75 |       16 |       94 |      207 |
| com.google.common.collect          | ImmutableCollection.java                   | src      |       36 |      247 |       37 |      190 |      510 |
| com.google.common.collect          | ImmutableEnumMap.java                      | src      |       22 |        5 |       19 |      106 |      152 |
| com.google.common.collect          | ImmutableEnumSet.java                      | src      |       25 |        5 |       27 |      115 |      172 |
| com.google.common.collect          | ImmutableList.java                         | src      |       94 |      304 |       50 |      508 |      956 |
| com.google.common.collect          | ImmutableListMultimap.java                 | src      |       54 |      199 |       20 |      301 |      574 |
| com.google.common.collect          | ImmutableMap.java                          | src      |      119 |      379 |       91 |      755 |     1344 |
| com.google.common.collect          | ImmutableMapEntry.java                     | src      |       19 |       24 |       22 |       72 |      137 |
| com.google.common.collect          | ImmutableMapEntrySet.java                  | src      |       27 |        6 |       16 |      112 |      161 |
| com.google.common.collect          | ImmutableMapKeySet.java                    | src      |       17 |        6 |       17 |       66 |      106 |
| com.google.common.collect          | ImmutableMapValues.java                    | src      |       21 |        6 |       24 |       98 |      149 |
| com.google.common.collect          | ImmutableMultimap.java                     | src      |       95 |      236 |       32 |      478 |      841 |
| com.google.common.collect          | ImmutableMultiset.java                     | src      |       76 |      204 |       22 |      368 |      670 |
| com.google.common.collect          | ImmutableRangeMap.java                     | src      |       46 |       79 |       15 |      326 |      466 |
| com.google.common.collect          | ImmutableRangeSet.java                     | src      |       98 |      162 |       21 |      615 |      896 |
| com.google.common.collect          | ImmutableSet.java                          | src      |       92 |      203 |      108 |      541 |      944 |
| com.google.common.collect          | ImmutableSetMultimap.java                  | src      |       71 |      239 |       35 |      400 |      745 |
| com.google.common.collect          | ImmutableSortedAsList.java                 | src      |       14 |        6 |       26 |       59 |      105 |
| com.google.common.collect          | ImmutableSortedMap.java                    | src      |      113 |      508 |       59 |      798 |     1478 |
| com.google.common.collect          | ImmutableSortedMultiset.java               | src      |       59 |      348 |       19 |      343 |      769 |
| com.google.common.collect          | ImmutableSortedSet.java                    | src      |       86 |      432 |       58 |      458 |     1034 |
| com.google.common.collect          | ImmutableTable.java                        | src      |       49 |      170 |       15 |      268 |      502 |
| com.google.common.collect          | IndexedImmutableSet.java                   | src      |       12 |        0 |       17 |       60 |       89 |
| com.google.common.collect          | Internal.java                              | src      |        5 |        8 |       15 |       16 |       44 |
| com.google.common.collect          | Interner.java                              | src      |        3 |       23 |       15 |       10 |       51 |
| com.google.common.collect          | Interners.java                             | src      |       25 |       44 |       23 |      104 |      196 |
| com.google.common.collect          | Iterables.java                             | src      |       75 |      509 |       38 |      464 |     1086 |
| com.google.common.collect          | Iterators.java                             | src      |      123 |      480 |       62 |      807 |     1472 |
| com.google.common.collect          | JdkBackedImmutableBiMap.java               | src      |       20 |        4 |       18 |      117 |      159 |
| com.google.common.collect          | JdkBackedImmutableMap.java                 | src      |       14 |        9 |       21 |       97 |      141 |
| com.google.common.collect          | JdkBackedImmutableMultiset.java            | src      |       12 |        6 |       14 |       67 |       99 |
| com.google.common.collect          | JdkBackedImmutableSet.java                 | src      |        9 |        6 |       14 |       36 |       65 |
| com.google.common.collect          | LexicographicalOrdering.java               | src      |        9 |        1 |       15 |       54 |       79 |
| com.google.common.collect          | LinkedHashMultimap.java                    | src      |       73 |      126 |       42 |      466 |      707 |
| com.google.common.collect          | LinkedHashMultiset.java                    | src      |       12 |       33 |       15 |       47 |      107 |
| com.google.common.collect          | LinkedListMultimap.java                    | src      |       86 |      146 |       52 |      575 |      859 |
| com.google.common.collect          | ListMultimap.java                          | src      |        7 |       52 |       15 |       23 |       97 |
| com.google.common.collect          | Lists.java                                 | src      |      128 |      384 |       29 |      704 |     1245 |
| com.google.common.collect          | MapDifference.java                         | src      |       13 |       55 |       15 |       29 |      112 |
| com.google.common.collect          | MapMaker.java                              | src      |       25 |      141 |       16 |      132 |      314 |
| com.google.common.collect          | MapMakerInternalMap.java                   | src      |      411 |      300 |      138 |     2069 |     2918 |
| com.google.common.collect          | Maps.java                                  | src      |      488 |     1248 |      134 |     2629 |     4499 |
| com.google.common.collect          | MinMaxPriorityQueue.java                   | src      |       89 |      244 |       80 |      569 |      982 |
| com.google.common.collect          | MoreCollectors.java                        | src      |       18 |       24 |       21 |      122 |      185 |
| com.google.common.collect          | Multimap.java                              | src      |       29 |      290 |       20 |       51 |      390 |
| com.google.common.collect          | MultimapBuilder.java                       | src      |       61 |      139 |       25 |      275 |      500 |
| com.google.common.collect          | Multimaps.java                             | src      |      211 |      836 |       30 |     1130 |     2207 |
| com.google.common.collect          | Multiset.java                              | src      |       35 |      341 |       22 |       85 |      483 |
| com.google.common.collect          | Multisets.java                             | src      |      125 |      252 |       42 |      768 |     1187 |
| com.google.common.collect          | MutableClassToInstanceMap.java             | src      |       25 |       24 |       21 |      131 |      201 |
| com.google.common.collect          | NaturalOrdering.java                       | src      |       13 |        1 |       20 |       53 |       87 |
| com.google.common.collect          | NullnessCasts.java                         | src      |        5 |       32 |       14 |       17 |       68 |
| com.google.common.collect          | NullsFirstOrdering.java                    | src      |       12 |        1 |       16 |       62 |       91 |
| com.google.common.collect          | NullsLastOrdering.java                     | src      |       12 |        1 |       16 |       62 |       91 |
| com.google.common.collect          | ObjectArrays.java                          | src      |       19 |       90 |       21 |      112 |      242 |
| com.google.common.collect          | Ordering.java                              | src      |       66 |      585 |       65 |      298 |     1014 |
| com.google.common.collect          | ParametricNullness.java                    | src      |        4 |       41 |       15 |       12 |       72 |
| com.google.common.collect          | PeekingIterator.java                       | src      |        5 |       35 |       15 |       19 |       74 |
| com.google.common.collect          | Platform.java                              | src      |       17 |       34 |       27 |       58 |      136 |
| com.google.common.collect          | Queues.java                                | src      |       35 |      200 |       31 |      227 |      493 |
| com.google.common.collect          | Range.java                                 | src      |       58 |      364 |       30 |      281 |      733 |
| com.google.common.collect          | RangeMap.java                              | src      |       18 |      119 |       20 |       35 |      192 |
| com.google.common.collect          | RangeSet.java                              | src      |       29 |      179 |       18 |       51 |      277 |
| com.google.common.collect          | RegularContiguousSet.java                  | src      |       37 |        5 |       20 |      214 |      276 |
| com.google.common.collect          | RegularImmutableAsList.java                | src      |       15 |        6 |       16 |       64 |      101 |
| com.google.common.collect          | RegularImmutableBiMap.java                 | src      |       47 |       10 |       25 |      271 |      353 |
| com.google.common.collect          | RegularImmutableList.java                  | src      |       17 |        5 |       20 |       64 |      106 |
| com.google.common.collect          | RegularImmutableMap.java                   | src      |       49 |       53 |       63 |      284 |      449 |
| com.google.common.collect          | RegularImmutableMultiset.java              | src      |       23 |       19 |       14 |      146 |      202 |
| com.google.common.collect          | RegularImmutableSet.java                   | src      |       19 |        5 |       20 |       96 |      140 |
| com.google.common.collect          | RegularImmutableSortedMultiset.java        | src      |       21 |        5 |       14 |      102 |      142 |
| com.google.common.collect          | RegularImmutableSortedSet.java             | src      |       45 |        7 |       28 |      255 |      335 |
| com.google.common.collect          | RegularImmutableTable.java                 | src      |       25 |        9 |       29 |      145 |      208 |
| com.google.common.collect          | ReverseNaturalOrdering.java                | src      |       20 |        1 |       17 |       65 |      103 |
| com.google.common.collect          | ReverseOrdering.java                       | src      |       20 |        1 |       16 |       79 |      116 |
| com.google.common.collect          | RowSortedTable.java                        | src      |        4 |       22 |       15 |       16 |       57 |
| com.google.common.collect          | Serialization.java                         | src      |       12 |       54 |       15 |       94 |      175 |
| com.google.common.collect          | SetMultimap.java                           | src      |        8 |       72 |       15 |       26 |      121 |
| com.google.common.collect          | Sets.java                                  | src      |      219 |      766 |       69 |     1289 |     2343 |
| com.google.common.collect          | SingletonImmutableBiMap.java               | src      |       17 |        7 |       17 |       67 |      108 |
| com.google.common.collect          | SingletonImmutableList.java                | src      |       14 |        5 |       16 |       54 |       89 |
| com.google.common.collect          | SingletonImmutableSet.java                 | src      |       15 |        6 |       18 |       55 |       94 |
| com.google.common.collect          | SingletonImmutableTable.java               | src      |       13 |        5 |       15 |       53 |       86 |
| com.google.common.collect          | SneakyThrows.java                          | src      |        5 |       21 |       15 |       15 |       56 |
| com.google.common.collect          | SortedIterable.java                        | src      |        4 |       14 |       13 |       11 |       42 |
| com.google.common.collect          | SortedIterables.java                       | src      |        6 |        9 |       14 |       32 |       61 |
| com.google.common.collect          | SortedLists.java                           | src      |       14 |       94 |       21 |      175 |      304 |
| com.google.common.collect          | SortedMapDifference.java                   | src      |        7 |        6 |       15 |       16 |       44 |
| com.google.common.collect          | SortedMultiset.java                        | src      |       14 |       88 |       15 |       32 |      149 |
| com.google.common.collect          | SortedMultisetBridge.java                  | src      |        3 |        7 |       15 |        9 |       34 |
| com.google.common.collect          | SortedMultisets.java                       | src      |       28 |        7 |       15 |      129 |      179 |
| com.google.common.collect          | SortedSetMultimap.java                     | src      |        8 |       68 |       16 |       25 |      117 |
| com.google.common.collect          | SparseImmutableTable.java                  | src      |       16 |        1 |       23 |      104 |      144 |
| com.google.common.collect          | StandardRowSortedTable.java                | src      |       17 |       29 |       20 |       73 |      139 |
| com.google.common.collect          | StandardTable.java                         | src      |      120 |       54 |       77 |      763 |     1014 |
| com.google.common.collect          | Streams.java                               | src      |       73 |      312 |       48 |      554 |      987 |
| com.google.common.collect          | Synchronized.java                          | src      |      332 |       12 |       45 |     1850 |     2239 |
| com.google.common.collect          | Table.java                                 | src      |       32 |      184 |       19 |       60 |      295 |
| com.google.common.collect          | TableCollectors.java                       | src      |       23 |        1 |       25 |      175 |      224 |
| com.google.common.collect          | Tables.java                                | src      |       87 |      169 |       26 |      447 |      729 |
| com.google.common.collect          | TopKSelector.java                          | src      |       26 |       88 |       33 |      150 |      297 |
| com.google.common.collect          | TransformedIterator.java                   | src      |        9 |        6 |       15 |       28 |       58 |
| com.google.common.collect          | TransformedListIterator.java               | src      |       10 |        6 |       15 |       40 |       71 |
| com.google.common.collect          | TreeBasedTable.java                        | src      |       42 |       75 |       25 |      199 |      341 |
| com.google.common.collect          | TreeMultimap.java                          | src      |       19 |       90 |       15 |       99 |      223 |
| com.google.common.collect          | TreeMultiset.java                          | src      |      112 |       62 |       68 |      862 |     1104 |
| com.google.common.collect          | TreeRangeMap.java                          | src      |      103 |       20 |       44 |      663 |      830 |
| com.google.common.collect          | TreeRangeSet.java                          | src      |       96 |       34 |       62 |      731 |      923 |
| com.google.common.collect          | TreeTraverser.java                         | src      |       28 |       84 |       17 |      167 |      296 |
| com.google.common.collect          | UnmodifiableIterator.java                  | src      |        4 |       17 |       15 |       15 |       51 |
| com.google.common.collect          | UnmodifiableListIterator.java              | src      |        5 |       19 |       15 |       22 |       61 |
| com.google.common.collect          | UnmodifiableSortedMultiset.java            | src      |       17 |        7 |       19 |       74 |      117 |
| com.google.common.collect          | UsingToStringOrdering.java                 | src      |        8 |        1 |       16 |       22 |       47 |
| com.google.common.collect          | package-info.java                          | src      |        2 |      105 |       15 |        5 |      127 |
+------------------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| com.google.common.escape           | ArrayBasedCharEscaper.java                 | src      |       11 |       63 |       24 |       52 |      150 |
| com.google.common.escape           | ArrayBasedEscaperMap.java                  | src      |        9 |       20 |       20 |       34 |       83 |
| com.google.common.escape           | ArrayBasedUnicodeEscaper.java              | src      |       12 |       66 |       42 |       85 |      205 |
| com.google.common.escape           | CharEscaper.java                           | src      |       20 |       59 |       26 |       75 |      180 |
| com.google.common.escape           | CharEscaperBuilder.java                    | src      |       14 |       29 |       19 |       65 |      127 |
| com.google.common.escape           | Escaper.java                               | src      |        6 |       59 |       14 |       15 |       94 |
| com.google.common.escape           | Escapers.java                              | src      |       18 |       84 |       18 |       71 |      191 |
| com.google.common.escape           | ParametricNullness.java                    | src      |        4 |       41 |       15 |       12 |       72 |
| com.google.common.escape           | Platform.java                              | src      |        6 |       11 |       14 |       17 |       48 |
| com.google.common.escape           | UnicodeEscaper.java                        | src      |       17 |      141 |       26 |      128 |      312 |
| com.google.common.escape           | package-info.java                          | src      |        2 |       12 |       13 |        5 |       32 |
+------------------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| com.google.common.eventbus         | AllowConcurrentEvents.java                 | src      |        3 |        9 |       13 |        8 |       33 |
| com.google.common.eventbus         | AsyncEventBus.java                         | src      |        6 |       30 |       13 |       13 |       62 |
| com.google.common.eventbus         | DeadEvent.java                             | src      |        9 |       27 |       13 |       21 |       70 |
| com.google.common.eventbus         | Dispatcher.java                            | src      |       25 |       38 |       32 |       98 |      193 |
| com.google.common.eventbus         | EventBus.java                              | src      |       22 |      167 |       15 |       99 |      303 |
| com.google.common.eventbus         | ParametricNullness.java                    | src      |        4 |       41 |       15 |       12 |       72 |
| com.google.common.eventbus         | Subscribe.java                             | src      |        3 |       14 |       13 |       10 |       40 |
| com.google.common.eventbus         | Subscriber.java                            | src      |       20 |       28 |       16 |       81 |      145 |
| com.google.common.eventbus         | SubscriberExceptionContext.java            | src      |        9 |       19 |       13 |       28 |       69 |
| com.google.common.eventbus         | SubscriberExceptionHandler.java            | src      |        2 |        6 |       13 |        4 |       25 |
| com.google.common.eventbus         | SubscriberRegistry.java                    | src      |       34 |       32 |       36 |      165 |      267 |
| com.google.common.eventbus         | package-info.java                          | src      |        2 |        7 |       13 |        5 |       27 |
+------------------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| com.google.common.graph            | AbstractBaseGraph.java                     | src      |       37 |       26 |       23 |      257 |      343 |
| com.google.common.graph            | AbstractDirectedNetworkConnections.java    | src      |       20 |        9 |       20 |       94 |      143 |
| com.google.common.graph            | AbstractGraph.java                         | src      |        7 |       10 |       15 |       33 |       65 |
| com.google.common.graph            | AbstractGraphBuilder.java                  | src      |        5 |       11 |       15 |       12 |       43 |
| com.google.common.graph            | AbstractNetwork.java                       | src      |       40 |       36 |       21 |      233 |      330 |
| com.google.common.graph            | AbstractUndirectedNetworkConnections.java  | src      |       15 |        8 |       17 |       66 |      106 |
| com.google.common.graph            | AbstractValueGraph.java                    | src      |       23 |       14 |       16 |      106 |      159 |
| com.google.common.graph            | ArchetypeGraph.java                        | src      |       17 |      132 |       24 |       18 |      191 |
| com.google.common.graph            | BaseGraph.java                             | src      |        8 |       46 |       21 |        8 |       83 |
| com.google.common.graph            | DirectedGraphConnections.java              | src      |       66 |       26 |       31 |      435 |      558 |
| com.google.common.graph            | DirectedMultiNetworkConnections.java       | src      |       19 |        7 |       15 |      104 |      145 |
| com.google.common.graph            | DirectedNetworkConnections.java            | src      |       10 |        7 |       15 |       34 |       66 |
| com.google.common.graph            | EdgesConnecting.java                       | src      |       10 |        9 |       15 |       35 |       69 |
| com.google.common.graph            | ElementOrder.java                          | src      |       20 |       77 |       15 |       94 |      206 |
| com.google.common.graph            | EndpointPair.java                          | src      |       36 |       54 |       25 |      136 |      251 |
| com.google.common.graph            | EndpointPairIterator.java                  | src      |       14 |       39 |       23 |       71 |      147 |
| com.google.common.graph            | ForwardingGraph.java                       | src      |       19 |       10 |       15 |       65 |      109 |
| com.google.common.graph            | ForwardingNetwork.java                     | src      |       30 |        7 |       15 |      111 |      163 |
| com.google.common.graph            | ForwardingValueGraph.java                  | src      |       22 |       11 |       15 |       79 |      127 |
| com.google.common.graph            | Graph.java                                 | src      |       23 |      257 |       27 |       42 |      349 |
| com.google.common.graph            | GraphBuilder.java                          | src      |       16 |       95 |       15 |       69 |      195 |
| com.google.common.graph            | GraphConnections.java                      | src      |       12 |       31 |       15 |       18 |       76 |
| com.google.common.graph            | GraphConstants.java                        | src      |        8 |        2 |       17 |       38 |       65 |
| com.google.common.graph            | Graphs.java                                | src      |       90 |      145 |       33 |      450 |      718 |
| com.google.common.graph            | GraphsBridgeMethods.java                   | src      |        4 |        4 |        0 |       12 |       20 |
| com.google.common.graph            | ImmutableGraph.java                        | src      |       18 |       83 |       20 |       80 |      201 |
| com.google.common.graph            | ImmutableNetwork.java                      | src      |       21 |      103 |       21 |       95 |      240 |
| com.google.common.graph            | ImmutableValueGraph.java                   | src      |       18 |       88 |       21 |       78 |      205 |
| com.google.common.graph            | IncidentEdgeSet.java                       | src      |       13 |       12 |       15 |       70 |      110 |
| com.google.common.graph            | InvalidatableSet.java                      | src      |        8 |        4 |        6 |       35 |       53 |
| com.google.common.graph            | MapIteratorCache.java                      | src      |       21 |       14 |       27 |       89 |      151 |
| com.google.common.graph            | MapRetrievalCache.java                     | src      |       16 |        6 |       23 |       65 |      110 |
| com.google.common.graph            | MultiEdgesConnecting.java                  | src      |        8 |        9 |       15 |       37 |       69 |
| com.google.common.graph            | MutableGraph.java                          | src      |        9 |       66 |       15 |       16 |      106 |
| com.google.common.graph            | MutableNetwork.java                        | src      |        8 |       74 |       15 |       14 |      111 |
| com.google.common.graph            | MutableValueGraph.java                     | src      |        9 |       74 |       15 |       17 |      115 |
| com.google.common.graph            | Network.java                               | src      |       28 |      364 |       27 |       37 |      456 |
| com.google.common.graph            | NetworkBuilder.java                        | src      |       17 |      101 |       15 |       67 |      200 |
| com.google.common.graph            | NetworkConnections.java                    | src      |       15 |       27 |       15 |       20 |       77 |
| com.google.common.graph            | ParametricNullness.java                    | src      |        4 |       41 |       15 |       12 |       72 |
| com.google.common.graph            | PredecessorsFunction.java                  | src      |        4 |       78 |       15 |        6 |      103 |
| com.google.common.graph            | StandardMutableGraph.java                  | src      |       11 |       11 |       15 |       38 |       75 |
| com.google.common.graph            | StandardMutableNetwork.java                | src      |       20 |       19 |       18 |      118 |      175 |
| com.google.common.graph            | StandardMutableValueGraph.java             | src      |       24 |       19 |       20 |      129 |      192 |
| com.google.common.graph            | StandardNetwork.java                       | src      |       27 |       24 |       20 |      137 |      208 |
| com.google.common.graph            | StandardValueGraph.java                    | src      |       25 |       22 |       17 |      124 |      188 |
| com.google.common.graph            | SuccessorsFunction.java                    | src      |        4 |       81 |       15 |        6 |      106 |
| com.google.common.graph            | Traverser.java                             | src      |       26 |      269 |       26 |      175 |      496 |
| com.google.common.graph            | UndirectedGraphConnections.java            | src      |       16 |        7 |       15 |       82 |      120 |
| com.google.common.graph            | UndirectedMultiNetworkConnections.java     | src      |       16 |        7 |       15 |       87 |      125 |
| com.google.common.graph            | UndirectedNetworkConnections.java          | src      |        9 |        7 |       15 |       27 |       58 |
| com.google.common.graph            | ValueGraph.java                            | src      |       28 |      301 |       27 |       46 |      402 |
| com.google.common.graph            | ValueGraphBuilder.java                     | src      |       16 |      104 |       15 |       68 |      203 |
| com.google.common.graph            | package-info.java                          | src      |        2 |        7 |       15 |        5 |       29 |
+------------------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| com.google.common.hash             | AbstractByteHasher.java                    | src      |       18 |       11 |       13 |      103 |      145 |
| com.google.common.hash             | AbstractCompositeHashFunction.java         | src      |       25 |       12 |       14 |      148 |      199 |
| com.google.common.hash             | AbstractHashFunction.java                  | src      |       12 |        5 |       13 |       51 |       81 |
| com.google.common.hash             | AbstractHasher.java                        | src      |       15 |        6 |       13 |      101 |      135 |
| com.google.common.hash             | AbstractNonStreamingHashFunction.java      | src      |       27 |       12 |       15 |      117 |      171 |
| com.google.common.hash             | AbstractStreamingHasher.java               | src      |       27 |       38 |       34 |      124 |      223 |
| com.google.common.hash             | BloomFilter.java                           | src      |       54 |      320 |       54 |      300 |      728 |
| com.google.common.hash             | BloomFilterStrategies.java                 | src      |       30 |       58 |       23 |      205 |      316 |
| com.google.common.hash             | ChecksumHashFunction.java                  | src      |       19 |        6 |       21 |      105 |      151 |
| com.google.common.hash             | Crc32cHashFunction.java                    | src      |       20 |        6 |       38 |      311 |      375 |
| com.google.common.hash             | FarmHashFingerprint64.java                 | src      |       50 |       17 |       38 |      463 |      568 |
| com.google.common.hash             | Fingerprint2011.java                       | src      |       24 |       24 |        9 |      142 |      199 |
| com.google.common.hash             | Funnel.java                                | src      |        4 |       29 |       13 |       10 |       56 |
| com.google.common.hash             | Funnels.java                               | src      |       47 |       46 |       13 |      175 |      281 |
| com.google.common.hash             | HashCode.java                              | src      |       56 |      105 |       19 |      242 |      422 |
| com.google.common.hash             | HashFunction.java                          | src      |       13 |      174 |       13 |       21 |      221 |
| com.google.common.hash             | Hasher.java                                | src      |       18 |       68 |       13 |       55 |      154 |
| com.google.common.hash             | Hashing.java                               | src      |       65 |      393 |       20 |      364 |      842 |
| com.google.common.hash             | HashingInputStream.java                    | src      |       12 |       35 |       13 |       52 |      112 |
| com.google.common.hash             | HashingOutputStream.java                   | src      |        9 |       16 |       20 |       33 |       78 |
| com.google.common.hash             | IgnoreJRERequirement.java                  | src      |        4 |        5 |       13 |        8 |       30 |
| com.google.common.hash             | ImmutableSupplier.java                     | src      |        3 |        4 |       14 |        7 |       28 |
| com.google.common.hash             | Java8Compatibility.java                    | src      |        7 |        4 |       13 |       19 |       43 |
| com.google.common.hash             | LittleEndianByteArray.java                 | src      |       36 |       75 |       47 |      199 |      357 |
| com.google.common.hash             | MacHashFunction.java                       | src      |       21 |        6 |       14 |      102 |      143 |
| com.google.common.hash             | MessageDigestHashFunction.java             | src      |       26 |        7 |       14 |      126 |      173 |
| com.google.common.hash             | Murmur3_128HashFunction.java               | src      |       31 |        7 |       23 |      157 |      218 |
| com.google.common.hash             | Murmur3_32HashFunction.java                | src      |       55 |        9 |       34 |      333 |      431 |
| com.google.common.hash             | ParametricNullness.java                    | src      |        4 |       41 |       15 |       12 |       72 |
| com.google.common.hash             | Platform.java                              | src      |        4 |        1 |       13 |        8 |       26 |
| com.google.common.hash             | PrimitiveSink.java                         | src      |       15 |       62 |       13 |       34 |      124 |
| com.google.common.hash             | SipHashFunction.java                       | src      |       26 |       13 |       32 |      115 |      186 |
| com.google.common.hash             | SneakyThrows.java                          | src      |        5 |       21 |       15 |       15 |       56 |
| com.google.common.hash             | package-info.java                          | src      |        2 |        6 |       14 |        5 |       27 |
+------------------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| com.google.common.html             | HtmlEscapers.java                          | src      |        6 |       29 |       16 |       19 |       70 |
| com.google.common.html             | ParametricNullness.java                    | src      |        4 |       41 |       15 |       12 |       72 |
| com.google.common.html             | package-info.java                          | src      |        2 |        8 |       13 |        5 |       28 |
+------------------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| com.google.common.io               | AppendableWriter.java                      | src      |       17 |       13 |       22 |       77 |      129 |
| com.google.common.io               | BaseEncoding.java                          | src      |      141 |      241 |       34 |      871 |     1287 |
| com.google.common.io               | ByteArrayDataInput.java                    | src      |       17 |       15 |       15 |       53 |      100 |
| com.google.common.io               | ByteArrayDataOutput.java                   | src      |       17 |       15 |       13 |       39 |       84 |
| com.google.common.io               | ByteProcessor.java                         | src      |        4 |       19 |       13 |       16 |       52 |
| com.google.common.io               | ByteSink.java                              | src      |       17 |       60 |       13 |       54 |      144 |
| com.google.common.io               | ByteSource.java                            | src      |       78 |      218 |       38 |      413 |      747 |
| com.google.common.io               | ByteStreams.java                           | src      |       97 |      220 |       45 |      635 |      997 |
| com.google.common.io               | CharSequenceReader.java                    | src      |       20 |        7 |       25 |      102 |      154 |
| com.google.common.io               | CharSink.java                              | src      |       17 |       84 |       13 |       61 |      175 |
| com.google.common.io               | CharSource.java                            | src      |       68 |      282 |       19 |      371 |      740 |
| com.google.common.io               | CharStreams.java                           | src      |       34 |      134 |       20 |      180 |      368 |
| com.google.common.io               | Closeables.java                            | src      |        7 |       64 |       22 |       47 |      140 |
| com.google.common.io               | Closer.java                                | src      |       17 |      114 |       24 |      100 |      255 |
| com.google.common.io               | CountingInputStream.java                   | src      |       15 |       12 |       14 |       63 |      104 |
| com.google.common.io               | CountingOutputStream.java                  | src      |       10 |       12 |       16 |       32 |       70 |
| com.google.common.io               | FileBackedOutputStream.java                | src      |       57 |       68 |       46 |      322 |      493 |
| com.google.common.io               | FileWriteMode.java                         | src      |        3 |        7 |       13 |        8 |       31 |
| com.google.common.io               | Files.java                                 | src      |       68 |      411 |       31 |      393 |      903 |
| com.google.common.io               | Flushables.java                            | src      |        6 |       25 |       13 |       34 |       78 |
| com.google.common.io               | IgnoreJRERequirement.java                  | src      |        4 |        5 |       13 |        8 |       30 |
| com.google.common.io               | InsecureRecursiveDeleteException.java      | src      |        5 |       13 |       15 |       16 |       49 |
| com.google.common.io               | Java8Compatibility.java                    | src      |        9 |        4 |       13 |       27 |       53 |
| com.google.common.io               | LineBuffer.java                            | src      |       11 |       37 |       15 |       55 |      118 |
| com.google.common.io               | LineProcessor.java                         | src      |        5 |       16 |       13 |       14 |       48 |
| com.google.common.io               | LineReader.java                            | src      |        7 |       18 |       15 |       45 |       85 |
| com.google.common.io               | LittleEndianDataInputStream.java           | src      |       28 |       88 |       13 |      123 |      252 |
| com.google.common.io               | LittleEndianDataOutputStream.java          | src      |       17 |       62 |       17 |       75 |      171 |
| com.google.common.io               | MoreFiles.java                             | src      |       75 |      224 |      121 |      442 |      862 |
| com.google.common.io               | MultiInputStream.java                      | src      |       13 |       13 |       13 |       82 |      121 |
| com.google.common.io               | MultiReader.java                           | src      |       10 |        7 |       13 |       65 |       95 |
| com.google.common.io               | ParametricNullness.java                    | src      |        4 |       41 |       15 |       12 |       72 |
| com.google.common.io               | PatternFilenameFilter.java                 | src      |        7 |       18 |       26 |       23 |       74 |
| com.google.common.io               | ReaderInputStream.java                     | src      |       27 |       53 |       37 |      143 |      260 |
| com.google.common.io               | RecursiveDeleteOption.java                 | src      |        3 |       19 |       15 |       11 |       48 |
| com.google.common.io               | Resources.java                             | src      |       20 |       86 |       17 |       89 |      212 |
| com.google.common.io               | TempFileCreator.java                       | src      |       33 |       19 |       51 |      253 |      356 |
| com.google.common.io               | package-info.java                          | src      |        2 |       15 |       13 |        5 |       35 |
+------------------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| com.google.common.math             | BigDecimalMath.java                        | src      |       14 |       76 |       17 |       70 |      177 |
| com.google.common.math             | BigIntegerMath.java                        | src      |       49 |       96 |       81 |      295 |      521 |
| com.google.common.math             | DoubleMath.java                            | src      |       33 |      191 |       43 |      265 |      532 |
| com.google.common.math             | DoubleUtils.java                           | src      |       18 |        7 |       42 |       64 |      131 |
| com.google.common.math             | IgnoreJRERequirement.java                  | src      |        4 |        5 |       13 |        8 |       30 |
| com.google.common.math             | IntMath.java                               | src      |       47 |      222 |       87 |      408 |      764 |
| com.google.common.math             | LinearTransformation.java                  | src      |       50 |       74 |       13 |      181 |      318 |
| com.google.common.math             | LongMath.java                              | src      |       78 |      270 |      194 |      831 |     1373 |
| com.google.common.math             | MathPreconditions.java                     | src      |       14 |        5 |       13 |       79 |      111 |
| com.google.common.math             | PairedStats.java                           | src      |       24 |      131 |       16 |      149 |      320 |
| com.google.common.math             | PairedStatsAccumulator.java                | src      |       18 |       91 |       31 |      104 |      244 |
| com.google.common.math             | ParametricNullness.java                    | src      |        4 |       41 |       15 |       12 |       72 |
| com.google.common.math             | Quantiles.java                             | src      |       54 |      295 |       66 |      290 |      705 |
| com.google.common.math             | Stats.java                                 | src      |       40 |      337 |       18 |      268 |      663 |
| com.google.common.math             | StatsAccumulator.java                      | src      |       29 |      194 |       34 |      173 |      430 |
| com.google.common.math             | ToDoubleRounder.java                       | src      |       10 |       12 |       16 |      110 |      148 |
| com.google.common.math             | package-info.java                          | src      |        2 |       10 |       13 |        5 |       30 |
+------------------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| com.google.common.net              | HostAndPort.java                           | src      |       27 |      121 |       16 |      153 |      317 |
| com.google.common.net              | HostSpecifier.java                         | src      |       20 |       60 |       25 |       72 |      177 |
| com.google.common.net              | HttpHeaders.java                           | src      |      185 |      711 |       17 |      190 |     1103 |
| com.google.common.net              | InetAddresses.java                         | src      |      102 |      477 |       58 |      607 |     1244 |
| com.google.common.net              | InternetDomainName.java                    | src      |       68 |      336 |       41 |      201 |      646 |
| com.google.common.net              | MediaType.java                             | src      |      180 |      703 |       42 |      500 |     1425 |
| com.google.common.net              | ParametricNullness.java                    | src      |        4 |       41 |       15 |       12 |       72 |
| com.google.common.net              | PercentEscaper.java                        | src      |       13 |       54 |       39 |      127 |      233 |
| com.google.common.net              | UrlEscapers.java                           | src      |       12 |       95 |       15 |       27 |      149 |
| com.google.common.net              | package-info.java                          | src      |        2 |        8 |       13 |        5 |       28 |
+------------------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| com.google.common.primitives       | Booleans.java                              | src      |       56 |      235 |       23 |      340 |      654 |
| com.google.common.primitives       | Bytes.java                                 | src      |       41 |      171 |       23 |      269 |      504 |
| com.google.common.primitives       | Chars.java                                 | src      |       59 |      292 |       22 |      372 |      745 |
| com.google.common.primitives       | Doubles.java                               | src      |       67 |      325 |       33 |      413 |      838 |
| com.google.common.primitives       | DoublesMethodsForWeb.java                  | src      |        3 |        4 |       13 |        4 |       24 |
| com.google.common.primitives       | Floats.java                                | src      |       64 |      316 |       28 |      384 |      792 |
| com.google.common.primitives       | FloatsMethodsForWeb.java                   | src      |        3 |        4 |       13 |        4 |       24 |
| com.google.common.primitives       | IgnoreJRERequirement.java                  | src      |        4 |        5 |       13 |        8 |       30 |
| com.google.common.primitives       | ImmutableDoubleArray.java                  | src      |       73 |      194 |       41 |      351 |      659 |
| com.google.common.primitives       | ImmutableIntArray.java                     | src      |       72 |      192 |       40 |      345 |      649 |
| com.google.common.primitives       | ImmutableLongArray.java                    | src      |       72 |      192 |       40 |      347 |      651 |
| com.google.common.primitives       | Ints.java                                  | src      |       70 |      351 |       48 |      414 |      883 |
| com.google.common.primitives       | IntsMethodsForWeb.java                     | src      |        3 |        4 |       13 |        4 |       24 |
| com.google.common.primitives       | Longs.java                                 | src      |       75 |      334 |       28 |      454 |      891 |
| com.google.common.primitives       | ParametricNullness.java                    | src      |        4 |       41 |       15 |       12 |       72 |
| com.google.common.primitives       | ParseRequest.java                          | src      |        7 |        1 |       14 |       33 |       55 |
| com.google.common.primitives       | Primitives.java                            | src      |       18 |       47 |       18 |       59 |      142 |
| com.google.common.primitives       | Shorts.java                                | src      |       67 |      305 |       22 |      399 |      793 |
| com.google.common.primitives       | ShortsMethodsForWeb.java                   | src      |        3 |        4 |       13 |        4 |       24 |
| com.google.common.primitives       | SignedBytes.java                           | src      |       18 |       91 |       16 |       93 |      218 |
| com.google.common.primitives       | UnsignedBytes.java                         | src      |       58 |      177 |       71 |      317 |      623 |
| com.google.common.primitives       | UnsignedInteger.java                       | src      |       29 |      110 |       15 |      106 |      260 |
| com.google.common.primitives       | UnsignedInts.java                          | src      |       30 |      190 |       14 |      160 |      394 |
| com.google.common.primitives       | UnsignedLong.java                          | src      |       31 |      108 |       21 |      114 |      274 |
| com.google.common.primitives       | UnsignedLongs.java                         | src      |       38 |      184 |       42 |      240 |      504 |
| com.google.common.primitives       | package-info.java                          | src      |        2 |       51 |       13 |        5 |       71 |
+------------------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| com.google.common.reflect          | AbstractInvocationHandler.java             | src      |       10 |       61 |       18 |       57 |      146 |
| com.google.common.reflect          | ClassPath.java                             | src      |       59 |      219 |       29 |      404 |      711 |
| com.google.common.reflect          | IgnoreJRERequirement.java                  | src      |        4 |        5 |       13 |        8 |       30 |
| com.google.common.reflect          | ImmutableTypeToInstanceMap.java            | src      |       20 |       62 |       13 |       81 |      176 |
| com.google.common.reflect          | Invokable.java                             | src      |       78 |      106 |       32 |      340 |      556 |
| com.google.common.reflect          | MutableTypeToInstanceMap.java              | src      |       25 |       19 |       19 |      113 |      176 |
| com.google.common.reflect          | Parameter.java                             | src      |       20 |       32 |       13 |       95 |      160 |
| com.google.common.reflect          | ParametricNullness.java                    | src      |        4 |       41 |       15 |       12 |       72 |
| com.google.common.reflect          | Reflection.java                            | src      |        9 |       35 |       13 |       32 |       89 |
| com.google.common.reflect          | TypeCapture.java                           | src      |        5 |        6 |       13 |       11 |       35 |
| com.google.common.reflect          | TypeParameter.java                         | src      |        9 |       14 |       23 |       32 |       78 |
| com.google.common.reflect          | TypeResolver.java                          | src      |       56 |       89 |       89 |      392 |      626 |
| com.google.common.reflect          | TypeToInstanceMap.java                     | src      |        7 |       49 |       13 |       16 |       85 |
| com.google.common.reflect          | TypeToken.java                             | src      |      138 |      339 |      107 |      891 |     1475 |
| com.google.common.reflect          | TypeVisitor.java                           | src      |       10 |       35 |       14 |       45 |      104 |
| com.google.common.reflect          | Types.java                                 | src      |       87 |       65 |       38 |      528 |      718 |
| com.google.common.reflect          | package-info.java                          | src      |        2 |        4 |       13 |        5 |       24 |
+------------------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| com.google.common.util.concurrent  | AbstractCatchingFuture.java                | src      |       23 |       11 |       27 |      187 |      248 |
| com.google.common.util.concurrent  | AbstractExecutionThreadService.java        | src      |       27 |       93 |       17 |      123 |      260 |
| com.google.common.util.concurrent  | AbstractFuture.java                        | src      |       65 |      227 |      184 |      582 |     1058 |
| com.google.common.util.concurrent  | AbstractFutureState.java                   | src      |       86 |       81 |      126 |      493 |      786 |
| com.google.common.util.concurrent  | AbstractIdleService.java                   | src      |       28 |       54 |       15 |      124 |      221 |
| com.google.common.util.concurrent  | AbstractListeningExecutorService.java      | src      |        8 |       18 |       13 |       41 |       80 |
| com.google.common.util.concurrent  | AbstractScheduledService.java              | src      |       69 |      227 |       97 |      366 |      759 |
| com.google.common.util.concurrent  | AbstractService.java                       | src      |       63 |      114 |       31 |      441 |      649 |
| com.google.common.util.concurrent  | AbstractTransformFuture.java               | src      |       22 |       11 |       75 |      155 |      263 |
| com.google.common.util.concurrent  | AggregateFuture.java                       | src      |       31 |       43 |      116 |      191 |      381 |
| com.google.common.util.concurrent  | AggregateFutureState.java                  | src      |       15 |       10 |       65 |       44 |      134 |
| com.google.common.util.concurrent  | AsyncCallable.java                         | src      |        3 |       16 |       13 |        9 |       41 |
| com.google.common.util.concurrent  | AsyncFunction.java                         | src      |        3 |       14 |       13 |        9 |       39 |
| com.google.common.util.concurrent  | AtomicDouble.java                          | src      |       30 |      146 |       13 |      120 |      309 |
| com.google.common.util.concurrent  | AtomicDoubleArray.java                     | src      |       28 |      155 |       16 |      131 |      330 |
| com.google.common.util.concurrent  | AtomicLongMap.java                         | src      |       36 |      140 |       15 |      166 |      357 |
| com.google.common.util.concurrent  | Atomics.java                               | src      |        7 |       55 |       13 |       35 |      110 |
| com.google.common.util.concurrent  | Callables.java                             | src      |        9 |       32 |       17 |       70 |      128 |
| com.google.common.util.concurrent  | ClosingFuture.java                         | src      |      151 |     1161 |       39 |     1090 |     2441 |
| com.google.common.util.concurrent  | CollectionFuture.java                      | src      |       16 |        3 |       20 |       73 |      112 |
| com.google.common.util.concurrent  | CombinedFuture.java                        | src      |       29 |        1 |       36 |      143 |      209 |
| com.google.common.util.concurrent  | CycleDetectingLockFactory.java             | src      |       94 |      342 |       68 |      474 |      978 |
| com.google.common.util.concurrent  | DirectExecutor.java                        | src      |        5 |        4 |       13 |       15 |       37 |
| com.google.common.util.concurrent  | DirectExecutorService.java                 | src      |       15 |        8 |       21 |       84 |      128 |
| com.google.common.util.concurrent  | ExecutionError.java                        | src      |        8 |       38 |       22 |       22 |       90 |
| com.google.common.util.concurrent  | ExecutionList.java                         | src      |       15 |       45 |       34 |       76 |      170 |
| com.google.common.util.concurrent  | ExecutionSequencer.java                    | src      |       32 |      129 |      139 |      178 |      478 |
| com.google.common.util.concurrent  | FakeTimeLimiter.java                       | src      |        9 |       11 |       13 |       67 |      100 |
| com.google.common.util.concurrent  | FluentFuture.java                          | src      |       20 |      288 |       13 |      105 |      426 |
| com.google.common.util.concurrent  | ForwardingBlockingDeque.java               | src      |       20 |       22 |       15 |       76 |      133 |
| com.google.common.util.concurrent  | ForwardingBlockingQueue.java               | src      |       12 |       15 |       13 |       50 |       90 |
| com.google.common.util.concurrent  | ForwardingCondition.java                   | src      |       10 |        1 |       13 |       40 |       64 |
| com.google.common.util.concurrent  | ForwardingExecutorService.java             | src      |       17 |       13 |       13 |       84 |      127 |
| com.google.common.util.concurrent  | ForwardingFluentFuture.java                | src      |       12 |       19 |       13 |       46 |       90 |
| com.google.common.util.concurrent  | ForwardingFuture.java                      | src      |       12 |       27 |       14 |       54 |      107 |
| com.google.common.util.concurrent  | ForwardingListenableFuture.java            | src      |        9 |       27 |       14 |       27 |       77 |
| com.google.common.util.concurrent  | ForwardingListeningExecutorService.java    | src      |        7 |       14 |       13 |       26 |       60 |
| com.google.common.util.concurrent  | ForwardingLock.java                        | src      |        9 |        1 |       13 |       35 |       58 |
| com.google.common.util.concurrent  | FutureCallback.java                        | src      |        4 |       16 |       13 |       10 |       43 |
| com.google.common.util.concurrent  | Futures.java                               | src      |       72 |      713 |      137 |      478 |     1400 |
| com.google.common.util.concurrent  | FuturesGetChecked.java                     | src      |       31 |       13 |       43 |      221 |      308 |
| com.google.common.util.concurrent  | GwtFluentFutureCatchingSpecialization.java | src      |        3 |        5 |       18 |        9 |       35 |
| com.google.common.util.concurrent  | GwtFuturesCatchingSpecialization.java      | src      |        3 |        6 |       18 |        7 |       34 |
| com.google.common.util.concurrent  | IgnoreJRERequirement.java                  | src      |        4 |        5 |       13 |        8 |       30 |
| com.google.common.util.concurrent  | ImmediateFuture.java                       | src      |       17 |        1 |       17 |       74 |      109 |
| com.google.common.util.concurrent  | Internal.java                              | src      |        5 |        8 |       15 |       16 |       44 |
| com.google.common.util.concurrent  | InterruptibleTask.java                     | src      |       22 |       25 |       78 |      137 |      262 |
| com.google.common.util.concurrent  | JdkFutureAdapters.java                     | src      |       16 |       53 |       30 |       69 |      168 |
| com.google.common.util.concurrent  | LazyLogger.java                            | src      |        6 |        1 |       24 |       27 |       58 |
| com.google.common.util.concurrent  | ListenableFuture.java                      | src      |        3 |      123 |       28 |       12 |      166 |
| com.google.common.util.concurrent  | ListenableFutureTask.java                  | src      |       12 |       31 |       15 |       50 |      108 |
| com.google.common.util.concurrent  | ListenableScheduledFuture.java             | src      |        3 |        6 |       13 |        7 |       29 |
| com.google.common.util.concurrent  | ListenerCallQueue.java                     | src      |       21 |       62 |       22 |      130 |      235 |
| com.google.common.util.concurrent  | ListeningExecutorService.java              | src      |       11 |       72 |       13 |       52 |      148 |
| com.google.common.util.concurrent  | ListeningScheduledExecutorService.java     | src      |       12 |       41 |       13 |       46 |      112 |
| com.google.common.util.concurrent  | Monitor.java                               | src      |       79 |      466 |      123 |      572 |     1240 |
| com.google.common.util.concurrent  | MoreExecutors.java                         | src      |       76 |      387 |       82 |      525 |     1070 |
| com.google.common.util.concurrent  | NullnessCasts.java                         | src      |        5 |       40 |       14 |       17 |       76 |
| com.google.common.util.concurrent  | OverflowAvoidingLockSupport.java           | src      |        6 |        4 |       16 |       13 |       39 |
| com.google.common.util.concurrent  | ParametricNullness.java                    | src      |        4 |       41 |       15 |       12 |       72 |
| com.google.common.util.concurrent  | Partially.java                             | src      |        4 |       15 |       13 |       17 |       49 |
| com.google.common.util.concurrent  | Platform.java                              | src      |       10 |        1 |       13 |       39 |       63 |
| com.google.common.util.concurrent  | RateLimiter.java                           | src      |       37 |      256 |       36 |      168 |      497 |
| com.google.common.util.concurrent  | Runnables.java                             | src      |        5 |       10 |       19 |       11 |       45 |
| com.google.common.util.concurrent  | SequentialExecutor.java                    | src      |       22 |       45 |       49 |      155 |      271 |
| com.google.common.util.concurrent  | Service.java                               | src      |       27 |      202 |       13 |       52 |      294 |
| com.google.common.util.concurrent  | ServiceManager.java                        | src      |       71 |      279 |       44 |      498 |      892 |
| com.google.common.util.concurrent  | ServiceManagerBridge.java                  | src      |        3 |        5 |       15 |       10 |       33 |
| com.google.common.util.concurrent  | SettableFuture.java                        | src      |        7 |       17 |       13 |       27 |       64 |
| com.google.common.util.concurrent  | SimpleTimeLimiter.java                     | src      |       32 |       19 |       17 |      215 |      283 |
| com.google.common.util.concurrent  | SmoothRateLimiter.java                     | src      |       31 |       95 |      133 |      135 |      394 |
| com.google.common.util.concurrent  | SneakyThrows.java                          | src      |        5 |       21 |       15 |       15 |       56 |
| com.google.common.util.concurrent  | Striped.java                               | src      |       59 |      165 |       55 |      275 |      554 |
| com.google.common.util.concurrent  | ThreadFactoryBuilder.java                  | src      |       13 |       95 |       19 |       99 |      226 |
| com.google.common.util.concurrent  | TimeLimiter.java                           | src      |       14 |      219 |       13 |       61 |      307 |
| com.google.common.util.concurrent  | TimeoutFuture.java                         | src      |       17 |        8 |       57 |      114 |      196 |
| com.google.common.util.concurrent  | TrustedListenableFutureTask.java           | src      |       29 |       15 |       24 |      117 |      185 |
| com.google.common.util.concurrent  | UncaughtExceptionHandlers.java             | src      |       10 |       22 |       15 |       43 |       90 |
| com.google.common.util.concurrent  | UncheckedExecutionException.java           | src      |        8 |       43 |       22 |       22 |       95 |
| com.google.common.util.concurrent  | UncheckedTimeoutException.java             | src      |        7 |       12 |       13 |       19 |       51 |
| com.google.common.util.concurrent  | Uninterruptibles.java                      | src      |       36 |      181 |       42 |      362 |      621 |
| com.google.common.util.concurrent  | WrappingExecutorService.java               | src      |       22 |       24 |       15 |      107 |      168 |
| com.google.common.util.concurrent  | WrappingScheduledExecutorService.java      | src      |        8 |        9 |       13 |       37 |       67 |
| com.google.common.util.concurrent  | package-info.java                          | src      |        2 |       12 |       13 |        5 |       32 |
+------------------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| com.google.common.xml              | ParametricNullness.java                    | src      |        4 |       41 |       15 |       12 |       72 |
| com.google.common.xml              | XmlEscapers.java                           | src      |       10 |       61 |       30 |       39 |      140 |
| com.google.common.xml              | package-info.java                          | src      |        2 |        8 |       13 |        5 |       28 |
+------------------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| com.google.thirdparty.publicsuffix | PublicSuffixPatterns.java                  | src      |        6 |       14 |       16 |      633 |      669 |
| com.google.thirdparty.publicsuffix | PublicSuffixTrie.java                      | src      |       31 |       76 |       22 |      141 |      270 |
| com.google.thirdparty.publicsuffix | PublicSuffixType.java                      | src      |       10 |       13 |       13 |       29 |       65 |
+------------------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| 19 package(s)                      | 611 file(s)                                | java     |    18564 |    51891 |    15495 |    97303 |   183253 |
+------------------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
```

The **module-info.java** of the **guava** module resides in the default package and is listed with an empty package name. The same run with **-DtrimPkgNames=true** shortens the **com.google.*** packages to their common prefix **com.google** and reads **Short Package Name** in the header. For example the **guava-testlib** module: 158 characters wide above, 140 characters wide below, narrow enough for printing:

```
$ mvn io.github.orhankupusoglu:sloc-maven-plugin:sloc -Ddisplay=false -Dsave=true -DtrimPkgNames=true

$ cat guava-testlib/sloc.txt
+--------------------------+---------------------------------------------+----------+----------+----------+----------+----------+----------+
| Short Package Name       | File Name                                   | Type     | Blank    | JavaDoc  | Comment  | Code     | Total    |
+--------------------------+---------------------------------------------+----------+----------+----------+----------+----------+----------+
|                          | module-info.java                            | src      |        2 |        1 |       13 |       15 |       31 |
+--------------------------+---------------------------------------------+----------+----------+----------+----------+----------+----------+
| collect.testing          | AbstractCollectionTestSuiteBuilder.java     | src      |        4 |        5 |       15 |       55 |       79 |
| collect.testing          | AbstractCollectionTester.java               | src      |       10 |       18 |       18 |       40 |       86 |
| collect.testing          | AbstractContainerTester.java                | src      |       33 |       73 |       29 |      128 |      263 |
| collect.testing          | AbstractIteratorTester.java                 | src      |       66 |       79 |       48 |      421 |      614 |
| collect.testing          | AbstractMapTester.java                      | src      |       39 |       36 |       19 |      167 |      261 |
| collect.testing          | AbstractTester.java                         | src      |       11 |       21 |       19 |       54 |      105 |
| collect.testing          | AnEnum.java                                 | src      |        3 |        5 |       15 |       11 |       34 |
| collect.testing          | BaseComparable.java                         | src      |        8 |        5 |       15 |       32 |       60 |
| collect.testing          | CollectionTestSuiteBuilder.java             | src      |       13 |        8 |       15 |       65 |      101 |
| collect.testing          | ConcurrentMapTestSuiteBuilder.java          | src      |        6 |        6 |       13 |       31 |       56 |
| collect.testing          | ConcurrentNavigableMapTestSuiteBuilder.java | src      |        7 |        6 |       15 |       26 |       54 |
| collect.testing          | DerivedCollectionGenerators.java            | src      |       88 |       11 |       41 |      422 |      562 |
| collect.testing          | DerivedComparable.java                      | src      |        4 |        5 |       15 |       11 |       35 |
| collect.testing          | DerivedGenerator.java                       | src      |        3 |       11 |       15 |        6 |       35 |
| collect.testing          | DerivedTestIteratorGenerator.java           | src      |        6 |        5 |       15 |       20 |       46 |
| collect.testing          | FeatureSpecificTestSuiteBuilder.java        | src      |       43 |       26 |       21 |      231 |      321 |
| collect.testing          | Helpers.java                                | src      |       75 |       47 |       31 |      401 |      554 |
| collect.testing          | IgnoreJRERequirement.java                   | src      |        4 |        5 |       13 |       10 |       32 |
| collect.testing          | IteratorFeature.java                        | src      |        6 |       24 |       15 |       18 |       63 |
| collect.testing          | IteratorTester.java                         | src      |        5 |       67 |       15 |       22 |      109 |
| collect.testing          | ListIteratorTester.java                     | src      |        5 |       13 |       15 |       29 |       62 |
| collect.testing          | ListTestSuiteBuilder.java                   | src      |       16 |       10 |       15 |      118 |      159 |
| collect.testing          | MapInterfaceTest.java                       | src      |      136 |       60 |       61 |     1261 |     1518 |
| collect.testing          | MapTestSuiteBuilder.java                    | src      |       31 |        5 |       22 |      230 |      288 |
| collect.testing          | MinimalCollection.java                      | src      |       20 |        6 |       25 |       86 |      137 |
| collect.testing          | MinimalIterable.java                        | src      |        8 |       30 |       18 |       31 |       87 |
| collect.testing          | MinimalSet.java                             | src      |       11 |        7 |       18 |       52 |       88 |
| collect.testing          | NavigableMapTestSuiteBuilder.java           | src      |       25 |        5 |       16 |      139 |      185 |
| collect.testing          | NavigableSetTestSuiteBuilder.java           | src      |       17 |        5 |       16 |      116 |      154 |
| collect.testing          | OneSizeGenerator.java                       | src      |       13 |        5 |       15 |       56 |       89 |
| collect.testing          | OneSizeTestContainerGenerator.java          | src      |        5 |       10 |       15 |       14 |       44 |
| collect.testing          | PerCollectionSizeTestSuiteBuilder.java      | src      |       18 |       15 |       17 |       89 |      139 |
| collect.testing          | Platform.java                               | src      |        6 |        7 |       16 |       18 |       47 |
| collect.testing          | QueueTestSuiteBuilder.java                  | src      |        7 |       11 |       15 |       37 |       70 |
| collect.testing          | ReserializingTestCollectionGenerator.java   | src      |       10 |        7 |       15 |       48 |       80 |
| collect.testing          | ReserializingTestSetGenerator.java          | src      |        6 |        7 |       15 |       17 |       45 |
| collect.testing          | SafeTreeMap.java                            | src      |       56 |        6 |       16 |      245 |      323 |
| collect.testing          | SafeTreeSet.java                            | src      |       44 |        6 |       16 |      186 |      252 |
| collect.testing          | SampleElements.java                         | src      |       24 |        5 |       21 |      110 |      160 |
| collect.testing          | SetTestSuiteBuilder.java                    | src      |       15 |        5 |       18 |       85 |      123 |
| collect.testing          | SortedMapInterfaceTest.java                 | src      |       11 |        6 |       16 |       93 |      126 |
| collect.testing          | SortedMapTestSuiteBuilder.java              | src      |       16 |       15 |       15 |       91 |      137 |
| collect.testing          | SortedSetTestSuiteBuilder.java              | src      |       13 |       11 |       17 |       80 |      121 |
| collect.testing          | SpliteratorTester.java                      | src      |       37 |       23 |       19 |      263 |      342 |
| collect.testing          | TestCharacterListGenerator.java             | src      |        7 |       11 |       15 |       31 |       64 |
| collect.testing          | TestCollectionGenerator.java                | src      |        3 |        5 |       15 |        9 |       32 |
| collect.testing          | TestCollidingSetGenerator.java              | src      |        5 |        6 |       15 |       21 |       47 |
| collect.testing          | TestContainerGenerator.java                 | src      |        6 |       27 |       15 |       15 |       63 |
| collect.testing          | TestEnumMapGenerator.java                   | src      |       11 |        6 |       15 |       51 |       83 |
| collect.testing          | TestEnumSetGenerator.java                   | src      |        8 |        6 |       20 |       35 |       69 |
| collect.testing          | TestIntegerSetGenerator.java                | src      |        7 |       16 |       15 |       32 |       70 |
| collect.testing          | TestIntegerSortedSetGenerator.java          | src      |        5 |        7 |       20 |       18 |       50 |
| collect.testing          | TestIteratorGenerator.java                  | src      |        3 |        6 |       15 |        7 |       31 |
| collect.testing          | TestListGenerator.java                      | src      |        3 |        5 |       15 |       11 |       34 |
| collect.testing          | TestMapEntrySetGenerator.java               | src      |       10 |        6 |       15 |       41 |       72 |
| collect.testing          | TestMapGenerator.java                       | src      |        4 |        5 |       15 |       12 |       36 |
| collect.testing          | TestQueueGenerator.java                     | src      |        3 |        5 |       15 |       11 |       34 |
| collect.testing          | TestSetGenerator.java                       | src      |        3 |        5 |       15 |       11 |       34 |
| collect.testing          | TestSortedMapGenerator.java                 | src      |        7 |       21 |       15 |       17 |       60 |
| collect.testing          | TestSortedSetGenerator.java                 | src      |        7 |       21 |       15 |       15 |       58 |
| collect.testing          | TestStringCollectionGenerator.java          | src      |        7 |        6 |       15 |       32 |       60 |
| collect.testing          | TestStringListGenerator.java                | src      |        7 |       10 |       15 |       31 |       63 |
| collect.testing          | TestStringMapGenerator.java                 | src      |       11 |        8 |       15 |       50 |       84 |
| collect.testing          | TestStringQueueGenerator.java               | src      |        7 |        6 |       15 |       32 |       60 |
| collect.testing          | TestStringSetGenerator.java                 | src      |        7 |       16 |       15 |       32 |       70 |
| collect.testing          | TestStringSortedMapGenerator.java           | src      |       10 |        5 |       15 |       39 |       69 |
| collect.testing          | TestStringSortedSetGenerator.java           | src      |       11 |        6 |       20 |       39 |       76 |
| collect.testing          | TestSubjectGenerator.java                   | src      |        3 |        7 |       15 |        9 |       34 |
| collect.testing          | TestUnhashableCollectionGenerator.java      | src      |        7 |        9 |       15 |       33 |       64 |
| collect.testing          | TestsForListsInJavaUtil.java                | src      |       29 |        6 |       18 |      299 |      352 |
| collect.testing          | TestsForMapsInJavaUtil.java                 | src      |       50 |       10 |       23 |      525 |      608 |
| collect.testing          | TestsForQueuesInJavaUtil.java               | src      |       26 |        6 |       21 |      218 |      271 |
| collect.testing          | TestsForSetsInJavaUtil.java                 | src      |       49 |       10 |       15 |      501 |      575 |
| collect.testing          | UnhashableObject.java                       | src      |        8 |        5 |       16 |       30 |       59 |
| collect.testing          | WrongType.java                              | src      |        3 |        4 |       15 |        6 |       28 |
| collect.testing          | package-info.java                           | src      |        0 |        0 |        0 |        2 |        2 |
+--------------------------+---------------------------------------------+----------+----------+----------+----------+----------+----------+
| collect.testing.features | CollectionFeature.java                      | src      |       19 |       46 |       15 |       46 |      126 |
| collect.testing.features | CollectionSize.java                         | src      |       12 |       19 |       21 |       46 |       98 |
| collect.testing.features | ConflictingRequirementsException.java       | src      |        8 |        5 |       15 |       27 |       55 |
| collect.testing.features | Feature.java                                | src      |        3 |        7 |       15 |        7 |       32 |
| collect.testing.features | FeatureUtil.java                            | src      |       21 |       89 |       20 |      188 |      318 |
| collect.testing.features | ListFeature.java                            | src      |       11 |        6 |       15 |       36 |       68 |
| collect.testing.features | MapFeature.java                             | src      |       10 |       29 |       15 |       41 |       95 |
| collect.testing.features | SetFeature.java                             | src      |        9 |        5 |       15 |       27 |       56 |
| collect.testing.features | TesterAnnotation.java                       | src      |        3 |        9 |       13 |       12 |       37 |
| collect.testing.features | TesterRequirements.java                     | src      |       13 |        6 |       15 |       50 |       84 |
| collect.testing.features | package-info.java                           | src      |        0 |        0 |        0 |        2 |        2 |
+--------------------------+---------------------------------------------+----------+----------+----------+----------+----------+----------+
| collect.testing.google   | AbstractBiMapTester.java                    | src      |        9 |        1 |       19 |       59 |       88 |
| collect.testing.google   | AbstractListMultimapTester.java             | src      |        8 |        5 |       13 |       31 |       57 |
| collect.testing.google   | AbstractMultimapTester.java                 | src      |       36 |       18 |       16 |      140 |      210 |
| collect.testing.google   | AbstractMultisetSetCountTester.java         | src      |       55 |       21 |       27 |      263 |      366 |
| collect.testing.google   | AbstractMultisetTester.java                 | src      |        4 |        5 |       15 |       13 |       37 |
| collect.testing.google   | BiMapClearTester.java                       | src      |        9 |        5 |       15 |       50 |       79 |
| collect.testing.google   | BiMapEntrySetTester.java                    | src      |        7 |        1 |       15 |       51 |       74 |
| collect.testing.google   | BiMapGenerators.java                        | src      |        7 |       11 |       15 |       43 |       76 |
| collect.testing.google   | BiMapInverseTester.java                     | src      |       14 |       21 |       17 |       64 |      116 |
| collect.testing.google   | BiMapPutTester.java                         | src      |       22 |        1 |       19 |       97 |      139 |
| collect.testing.google   | BiMapRemoveTester.java                      | src      |       10 |        5 |       15 |       58 |       88 |
| collect.testing.google   | BiMapTestSuiteBuilder.java                  | src      |       20 |        6 |       24 |      114 |      164 |
| collect.testing.google   | DerivedGoogleCollectionGenerators.java      | src      |       35 |        6 |       17 |      163 |      221 |
| collect.testing.google   | GoogleHelpers.java                          | src      |        6 |        5 |       15 |       13 |       39 |
| collect.testing.google   | ListGenerators.java                         | src      |       17 |        5 |       15 |      106 |      143 |
| collect.testing.google   | ListMultimapAsMapTester.java                | src      |        9 |        7 |       18 |       69 |      103 |
| collect.testing.google   | ListMultimapEqualsTester.java               | src      |        4 |        5 |       13 |       20 |       42 |
| collect.testing.google   | ListMultimapPutAllTester.java               | src      |        8 |        5 |       13 |       22 |       48 |
| collect.testing.google   | ListMultimapPutTester.java                  | src      |       13 |        5 |       14 |       43 |       75 |
| collect.testing.google   | ListMultimapRemoveTester.java               | src      |       17 |        5 |       13 |       64 |       99 |
| collect.testing.google   | ListMultimapReplaceValuesTester.java        | src      |        5 |        5 |       13 |       20 |       43 |
| collect.testing.google   | ListMultimapTestSuiteBuilder.java           | src      |       13 |        6 |       15 |      108 |      142 |
| collect.testing.google   | MapGenerators.java                          | src      |       27 |       10 |       16 |      195 |      248 |
| collect.testing.google   | MultimapAsMapGetTester.java                 | src      |       17 |        5 |       15 |       94 |      131 |
| collect.testing.google   | MultimapAsMapTester.java                    | src      |       15 |        5 |       13 |      104 |      137 |
| collect.testing.google   | MultimapClearTester.java                    | src      |       15 |        5 |       16 |       99 |      135 |
| collect.testing.google   | MultimapContainsEntryTester.java            | src      |       10 |        5 |       15 |       47 |       77 |
| collect.testing.google   | MultimapContainsKeyTester.java              | src      |       12 |        5 |       15 |       53 |       85 |
| collect.testing.google   | MultimapContainsValueTester.java            | src      |        8 |        5 |       15 |       34 |       62 |
| collect.testing.google   | MultimapEntriesTester.java                  | src      |       13 |        5 |       18 |       90 |      126 |
| collect.testing.google   | MultimapEqualsTester.java                   | src      |        9 |        5 |       13 |       67 |       94 |
| collect.testing.google   | MultimapFeature.java                        | src      |        9 |        5 |       15 |       30 |       59 |
| collect.testing.google   | MultimapForEachTester.java                  | src      |        4 |        5 |       15 |       25 |       49 |
| collect.testing.google   | MultimapGetTester.java                      | src      |       19 |        5 |       15 |      111 |      150 |
| collect.testing.google   | MultimapKeySetTester.java                   | src      |        8 |        5 |       13 |       51 |       77 |
| collect.testing.google   | MultimapKeysTester.java                     | src      |       10 |        5 |       13 |       73 |      101 |
| collect.testing.google   | MultimapPutAllMultimapTester.java           | src      |       11 |        5 |       16 |       70 |      102 |
| collect.testing.google   | MultimapPutIterableTester.java              | src      |       25 |        5 |       24 |      135 |      189 |
| collect.testing.google   | MultimapPutTester.java                      | src      |       40 |        5 |       15 |      158 |      218 |
| collect.testing.google   | MultimapRemoveAllTester.java                | src      |       14 |        5 |       15 |       56 |       90 |
| collect.testing.google   | MultimapRemoveEntryTester.java              | src      |       29 |        5 |       15 |      132 |      181 |
| collect.testing.google   | MultimapReplaceValuesTester.java            | src      |       15 |        5 |       15 |       94 |      129 |
| collect.testing.google   | MultimapSizeTester.java                     | src      |       15 |        5 |       15 |       73 |      108 |
| collect.testing.google   | MultimapTestSuiteBuilder.java               | src      |       78 |        6 |       25 |      586 |      695 |
| collect.testing.google   | MultimapToStringTester.java                 | src      |        8 |        5 |       13 |       42 |       68 |
| collect.testing.google   | MultimapValuesTester.java                   | src      |        6 |        5 |       13 |       40 |       64 |
| collect.testing.google   | MultisetAddTester.java                      | src      |       15 |        5 |       15 |       75 |      110 |
| collect.testing.google   | MultisetContainsTester.java                 | src      |        5 |        5 |       13 |       16 |       39 |
| collect.testing.google   | MultisetCountTester.java                    | src      |       12 |        9 |       15 |       54 |       90 |
| collect.testing.google   | MultisetElementSetTester.java               | src      |       10 |        9 |       15 |       74 |      108 |
| collect.testing.google   | MultisetEntrySetTester.java                 | src      |       21 |        5 |       15 |      204 |      245 |
| collect.testing.google   | MultisetEqualsTester.java                   | src      |        7 |        5 |       13 |       36 |       61 |
| collect.testing.google   | MultisetFeature.java                        | src      |        7 |        9 |       15 |       26 |       57 |
| collect.testing.google   | MultisetForEachEntryTester.java             | src      |        7 |        9 |       15 |       46 |       77 |
| collect.testing.google   | MultisetIteratorTester.java                 | src      |        8 |       10 |       13 |       76 |      107 |
| collect.testing.google   | MultisetNavigationTester.java               | src      |       82 |        6 |       15 |      535 |      638 |
| collect.testing.google   | MultisetReadsTester.java                    | src      |       16 |        6 |       15 |       75 |      112 |
| collect.testing.google   | MultisetRemoveTester.java                   | src      |       19 |       10 |       16 |      136 |      181 |
| collect.testing.google   | MultisetSerializationTester.java            | src      |        5 |        6 |       15 |       20 |       46 |
| collect.testing.google   | MultisetSetCountConditionallyTester.java    | src      |       14 |        6 |       21 |       64 |      105 |
| collect.testing.google   | MultisetSetCountUnconditionallyTester.java  | src      |        5 |        6 |       15 |       21 |       47 |
| collect.testing.google   | MultisetTestSuiteBuilder.java               | src      |       34 |        7 |       16 |      228 |      285 |
| collect.testing.google   | SetGenerators.java                          | src      |       47 |       13 |       41 |      336 |      437 |
| collect.testing.google   | SetMultimapAsMapTester.java                 | src      |        9 |        7 |       18 |       70 |      104 |
| collect.testing.google   | SetMultimapEqualsTester.java                | src      |        4 |        5 |       13 |       20 |       42 |
| collect.testing.google   | SetMultimapPutAllTester.java                | src      |        9 |        5 |       13 |       23 |       50 |
| collect.testing.google   | SetMultimapPutTester.java                   | src      |       10 |        5 |       14 |       43 |       72 |
| collect.testing.google   | SetMultimapReplaceValuesTester.java         | src      |        6 |        5 |       13 |       21 |       45 |
| collect.testing.google   | SetMultimapTestSuiteBuilder.java            | src      |       16 |        6 |       15 |      115 |      152 |
| collect.testing.google   | SortedMapGenerators.java                    | src      |       14 |       15 |       15 |       99 |      143 |
| collect.testing.google   | SortedMultisetTestSuiteBuilder.java         | src      |       40 |       21 |       22 |      230 |      313 |
| collect.testing.google   | SortedSetMultimapAsMapTester.java           | src      |        6 |        7 |       13 |       34 |       60 |
| collect.testing.google   | SortedSetMultimapGetTester.java             | src      |        3 |        5 |       15 |       10 |       33 |
| collect.testing.google   | SortedSetMultimapTestSuiteBuilder.java      | src      |        8 |        6 |       15 |       70 |       99 |
| collect.testing.google   | TestBiMapGenerator.java                     | src      |        4 |        5 |       15 |       14 |       38 |
| collect.testing.google   | TestEnumMultisetGenerator.java              | src      |        8 |        6 |       20 |       37 |       71 |
| collect.testing.google   | TestListMultimapGenerator.java              | src      |        3 |        5 |       15 |        9 |       32 |
| collect.testing.google   | TestMultimapGenerator.java                  | src      |        8 |        5 |       15 |       20 |       48 |
| collect.testing.google   | TestMultisetGenerator.java                  | src      |        3 |        5 |       15 |       13 |       36 |
| collect.testing.google   | TestSetMultimapGenerator.java               | src      |        3 |        5 |       15 |        6 |       29 |
| collect.testing.google   | TestStringBiMapGenerator.java               | src      |       11 |        9 |       15 |       51 |       86 |
| collect.testing.google   | TestStringListMultimapGenerator.java        | src      |       14 |        6 |       15 |       66 |      101 |
| collect.testing.google   | TestStringMultisetGenerator.java            | src      |        7 |        6 |       15 |       33 |       61 |
| collect.testing.google   | TestStringSetMultimapGenerator.java         | src      |       13 |        6 |       15 |       66 |      100 |
| collect.testing.google   | UnmodifiableCollectionTests.java            | src      |       56 |       87 |       32 |      183 |      358 |
| collect.testing.google   | package-info.java                           | src      |        0 |        0 |        0 |        2 |        2 |
+--------------------------+---------------------------------------------+----------+----------+----------+----------+----------+----------+
| collect.testing.suites   | package-info.java                           | src      |        0 |        0 |        0 |        2 |        2 |
+--------------------------+---------------------------------------------+----------+----------+----------+----------+----------+----------+
| collect.testing.testers  | AbstractListIndexOfTester.java              | src      |       12 |        7 |       15 |       54 |       88 |
| collect.testing.testers  | AbstractListTester.java                     | src      |        6 |       16 |       21 |       34 |       77 |
| collect.testing.testers  | AbstractQueueTester.java                    | src      |        3 |        5 |       15 |       10 |       33 |
| collect.testing.testers  | AbstractSetTester.java                      | src      |        3 |        3 |       20 |       10 |       36 |
| collect.testing.testers  | CollectionAddAllTester.java                 | src      |       17 |       26 |       19 |      122 |      184 |
| collect.testing.testers  | CollectionAddTester.java                    | src      |       12 |       29 |       15 |       75 |      131 |
| collect.testing.testers  | CollectionClearTester.java                  | src      |        7 |        6 |       15 |       44 |       72 |
| collect.testing.testers  | CollectionContainsAllTester.java            | src      |       13 |        8 |       15 |       68 |      104 |
| collect.testing.testers  | CollectionContainsTester.java               | src      |       10 |        7 |       15 |       46 |       78 |
| collect.testing.testers  | CollectionCreationTester.java               | src      |        7 |       12 |       15 |       33 |       67 |
| collect.testing.testers  | CollectionEqualsTester.java                 | src      |        6 |        5 |       19 |       23 |       53 |
| collect.testing.testers  | CollectionForEachTester.java                | src      |        5 |        6 |       15 |       26 |       52 |
| collect.testing.testers  | CollectionIsEmptyTester.java                | src      |        5 |        6 |       15 |       16 |       42 |
| collect.testing.testers  | CollectionIteratorTester.java               | src      |       15 |        6 |       16 |      110 |      147 |
| collect.testing.testers  | CollectionRemoveAllTester.java              | src      |       19 |        8 |       23 |      143 |      193 |
| collect.testing.testers  | CollectionRemoveIfTester.java               | src      |        9 |        6 |       15 |       64 |       94 |
| collect.testing.testers  | CollectionRemoveTester.java                 | src      |       14 |        6 |       15 |      103 |      138 |
| collect.testing.testers  | CollectionRetainAllTester.java              | src      |       50 |        7 |       38 |      222 |      317 |
| collect.testing.testers  | CollectionSerializationEqualTester.java     | src      |        4 |        6 |       20 |       14 |       44 |
| collect.testing.testers  | CollectionSerializationTester.java          | src      |        4 |        5 |       16 |       14 |       39 |
| collect.testing.testers  | CollectionSizeTester.java                   | src      |        3 |        6 |       15 |        9 |       33 |
| collect.testing.testers  | CollectionSpliteratorTester.java            | src      |       11 |        6 |       17 |       53 |       87 |
| collect.testing.testers  | CollectionStreamTester.java                 | src      |        7 |        6 |       19 |       21 |       53 |
| collect.testing.testers  | CollectionToArrayTester.java                | src      |       21 |       20 |       17 |      142 |      200 |
| collect.testing.testers  | CollectionToStringTester.java               | src      |        8 |        6 |       15 |       43 |       72 |
| collect.testing.testers  | ConcurrentMapPutIfAbsentTester.java         | src      |       13 |        7 |       15 |       93 |      128 |
| collect.testing.testers  | ConcurrentMapRemoveTester.java              | src      |       12 |        6 |       18 |       71 |      107 |
| collect.testing.testers  | ConcurrentMapReplaceEntryTester.java        | src      |       16 |        7 |       20 |      102 |      145 |
| collect.testing.testers  | ConcurrentMapReplaceTester.java             | src      |       11 |        7 |       17 |       66 |      101 |
| collect.testing.testers  | IgnoreJRERequirement.java                   | src      |        4 |        5 |       13 |       10 |       32 |
| collect.testing.testers  | ListAddAllAtIndexTester.java                | src      |       17 |        6 |       19 |      121 |      163 |
| collect.testing.testers  | ListAddAllTester.java                       | src      |        6 |        6 |       15 |       31 |       58 |
| collect.testing.testers  | ListAddAtIndexTester.java                   | src      |       15 |       10 |       19 |       92 |      136 |
| collect.testing.testers  | ListAddTester.java                          | src      |        8 |       10 |       19 |       43 |       80 |
| collect.testing.testers  | ListCreationTester.java                     | src      |        5 |        7 |       15 |       18 |       45 |
| collect.testing.testers  | ListEqualsTester.java                       | src      |       10 |        5 |       15 |       60 |       90 |
| collect.testing.testers  | ListGetTester.java                          | src      |        6 |        6 |       16 |       15 |       43 |
| collect.testing.testers  | ListHashCodeTester.java                     | src      |        5 |        9 |       15 |       24 |       53 |
| collect.testing.testers  | ListIndexOfTester.java                      | src      |        6 |        6 |       15 |       27 |       54 |
| collect.testing.testers  | ListLastIndexOfTester.java                  | src      |        6 |        6 |       15 |       29 |       56 |
| collect.testing.testers  | ListListIteratorTester.java                 | src      |       12 |       17 |       20 |       77 |      126 |
| collect.testing.testers  | ListRemoveAllTester.java                    | src      |        6 |        6 |       16 |       25 |       53 |
| collect.testing.testers  | ListRemoveAtIndexTester.java                | src      |       11 |        6 |       15 |       65 |       97 |
| collect.testing.testers  | ListRemoveTester.java                       | src      |        5 |        6 |       15 |       31 |       57 |
| collect.testing.testers  | ListReplaceAllTester.java                   | src      |        6 |        6 |       15 |       34 |       61 |
| collect.testing.testers  | ListRetainAllTester.java                    | src      |        6 |        6 |       15 |       43 |       70 |
| collect.testing.testers  | ListSetTester.java                          | src      |       15 |       15 |       15 |       89 |      134 |
| collect.testing.testers  | ListSubListTester.java                      | src      |       32 |       26 |       25 |      260 |      343 |
| collect.testing.testers  | ListToArrayTester.java                      | src      |        8 |        6 |       16 |       24 |       54 |
| collect.testing.testers  | MapClearTester.java                         | src      |        9 |        7 |       15 |       59 |       90 |
| collect.testing.testers  | MapComputeIfAbsentTester.java               | src      |       14 |        6 |       16 |      164 |      200 |
| collect.testing.testers  | MapComputeIfPresentTester.java              | src      |       14 |        6 |       15 |      141 |      176 |
| collect.testing.testers  | MapComputeTester.java                       | src      |       12 |        6 |       17 |      165 |      200 |
| collect.testing.testers  | MapContainsKeyTester.java                   | src      |       11 |        6 |       16 |       56 |       89 |
| collect.testing.testers  | MapContainsValueTester.java                 | src      |       10 |        7 |       16 |       48 |       81 |
| collect.testing.testers  | MapCreationTester.java                      | src      |       17 |       13 |       15 |       93 |      138 |
| collect.testing.testers  | MapEntrySetTester.java                      | src      |       22 |        7 |       17 |      155 |      201 |
| collect.testing.testers  | MapEqualsTester.java                        | src      |       17 |        6 |       15 |       90 |      128 |
| collect.testing.testers  | MapForEachTester.java                       | src      |        7 |        6 |       15 |       49 |       77 |
| collect.testing.testers  | MapGetOrDefaultTester.java                  | src      |       13 |        6 |       15 |       89 |      123 |
| collect.testing.testers  | MapGetTester.java                           | src      |       10 |        7 |       15 |       48 |       80 |
| collect.testing.testers  | MapHashCodeTester.java                      | src      |       11 |        6 |       15 |       52 |       84 |
| collect.testing.testers  | MapIsEmptyTester.java                       | src      |        5 |        6 |       15 |       16 |       42 |
| collect.testing.testers  | MapMergeTester.java                         | src      |       13 |       10 |       15 |      163 |      201 |
| collect.testing.testers  | MapPutAllTester.java                        | src      |       20 |       12 |       15 |      132 |      179 |
| collect.testing.testers  | MapPutIfAbsentTester.java                   | src      |       15 |        6 |       16 |       99 |      136 |
| collect.testing.testers  | MapPutTester.java                           | src      |       27 |       13 |       15 |      175 |      230 |
| collect.testing.testers  | MapRemoveEntryTester.java                   | src      |       11 |        6 |       18 |       65 |      100 |
| collect.testing.testers  | MapRemoveTester.java                        | src      |       15 |        7 |       15 |      107 |      144 |
| collect.testing.testers  | MapReplaceAllTester.java                    | src      |       17 |        6 |       26 |      154 |      203 |
| collect.testing.testers  | MapReplaceEntryTester.java                  | src      |       16 |        6 |       20 |      100 |      142 |
| collect.testing.testers  | MapReplaceTester.java                       | src      |       11 |        6 |       17 |       69 |      103 |
| collect.testing.testers  | MapSerializationTester.java                 | src      |        4 |        5 |       15 |       16 |       40 |
| collect.testing.testers  | MapSizeTester.java                          | src      |        3 |        6 |       15 |        9 |       33 |
| collect.testing.testers  | MapToStringTester.java                      | src      |       10 |        7 |       13 |       57 |       87 |
| collect.testing.testers  | NavigableMapNavigationTester.java           | src      |       36 |        8 |       16 |      207 |      267 |
| collect.testing.testers  | NavigableSetNavigationTester.java           | src      |       30 |       13 |       31 |      177 |      251 |
| collect.testing.testers  | Platform.java                               | src      |        7 |        8 |       15 |       18 |       48 |
| collect.testing.testers  | QueueElementTester.java                     | src      |        6 |        6 |       15 |       29 |       56 |
| collect.testing.testers  | QueueOfferTester.java                       | src      |        6 |        6 |       15 |       25 |       52 |
| collect.testing.testers  | QueuePeekTester.java                        | src      |        6 |        6 |       15 |       27 |       54 |
| collect.testing.testers  | QueuePollTester.java                        | src      |        6 |        6 |       15 |       30 |       57 |
| collect.testing.testers  | QueueRemoveTester.java                      | src      |        6 |        6 |       15 |       32 |       59 |
| collect.testing.testers  | SetAddAllTester.java                        | src      |        6 |        6 |       15 |       29 |       56 |
| collect.testing.testers  | SetAddTester.java                           | src      |        6 |       10 |       15 |       33 |       64 |
| collect.testing.testers  | SetCreationTester.java                      | src      |        9 |        7 |       15 |       48 |       79 |
| collect.testing.testers  | SetEqualsTester.java                        | src      |       13 |        5 |       15 |       61 |       94 |
| collect.testing.testers  | SetHashCodeTester.java                      | src      |        7 |       10 |       15 |       47 |       79 |
| collect.testing.testers  | SetRemoveTester.java                        | src      |        4 |        6 |       15 |       17 |       42 |
| collect.testing.testers  | SortedMapNavigationTester.java              | src      |       20 |        7 |       16 |      130 |      173 |
| collect.testing.testers  | SortedSetNavigationTester.java              | src      |       12 |        7 |       16 |       58 |       93 |
| collect.testing.testers  | TestExceptions.java                         | src      |        9 |        1 |       15 |       16 |       41 |
| collect.testing.testers  | package-info.java                           | src      |        0 |        0 |        0 |        2 |        2 |
+--------------------------+---------------------------------------------+----------+----------+----------+----------+----------+----------+
| escape.testing           | EscaperAsserts.java                         | src      |       10 |       45 |       17 |       40 |      112 |
| escape.testing           | package-info.java                           | src      |        2 |        6 |       15 |        3 |       26 |
+--------------------------+---------------------------------------------+----------+----------+----------+----------+----------+----------+
| testing                  | AbstractClassInitializationTest.java        | src      |       13 |       24 |       16 |       98 |      151 |
| testing                  | AbstractPackageSanityTests.java             | src      |       26 |      160 |       25 |      225 |      436 |
| testing                  | ArbitraryInstances.java                     | src      |       35 |       35 |       27 |      452 |      549 |
| testing                  | ClassPathUtil.java                          | src      |        6 |        6 |       15 |       40 |       67 |
| testing                  | ClassSanityTester.java                      | src      |       48 |      206 |       30 |      566 |      850 |
| testing                  | CollectorTester.java                        | src      |       14 |       34 |       15 |      116 |      179 |
| testing                  | DummyProxy.java                             | src      |       14 |       10 |       19 |       93 |      136 |
| testing                  | EqualsTester.java                           | src      |       11 |       66 |       15 |       75 |      167 |
| testing                  | EquivalenceTester.java                      | src      |       11 |       30 |       19 |       60 |      120 |
| testing                  | FakeTicker.java                             | src      |       11 |       32 |       15 |       52 |      110 |
| testing                  | ForwardingWrapperTester.java                | src      |       20 |       29 |       23 |      174 |      246 |
| testing                  | FreshValueGenerator.java                    | src      |      150 |       35 |       31 |      814 |     1030 |
| testing                  | GcFinalization.java                         | src      |       27 |      149 |       41 |      197 |      414 |
| testing                  | IgnoreJRERequirement.java                   | src      |        4 |        5 |       13 |        8 |       30 |
| testing                  | NullPointerTester.java                      | src      |       65 |      117 |       58 |      467 |      707 |
| testing                  | Platform.java                               | src      |        5 |        6 |       15 |       28 |       54 |
| testing                  | RelationshipTester.java                     | src      |       19 |       13 |       18 |      120 |      170 |
| testing                  | SerializableTester.java                     | src      |        5 |       53 |       15 |       22 |       95 |
| testing                  | SloppyTearDown.java                         | src      |        5 |       10 |       15 |       19 |       49 |
| testing                  | TearDown.java                               | src      |        3 |       22 |       15 |        9 |       49 |
| testing                  | TearDownAccepter.java                       | src      |        3 |       13 |       15 |       10 |       41 |
| testing                  | TearDownStack.java                          | src      |       12 |       16 |       19 |       61 |      108 |
| testing                  | TestLogHandler.java                         | src      |       10 |       31 |       22 |       40 |      103 |
| testing                  | package-info.java                           | src      |        2 |        4 |       15 |        5 |       26 |
+--------------------------+---------------------------------------------+----------+----------+----------+----------+----------+----------+
| util.concurrent.testing  | AbstractListenableFutureTest.java           | src      |       43 |       20 |       24 |      108 |      195 |
| util.concurrent.testing  | MockFutureListener.java                     | src      |       12 |       18 |       19 |       39 |       88 |
| util.concurrent.testing  | SameThreadScheduledExecutorService.java     | src      |       26 |        8 |       15 |      138 |      187 |
| util.concurrent.testing  | TestingExecutors.java                       | src      |       22 |       48 |       15 |       87 |      172 |
+--------------------------+---------------------------------------------+----------+----------+----------+----------+----------+----------+
| 9 package(s)             | 298 file(s)                                 | java     |     4405 |     3850 |     5034 |    26178 |    39467 |
+--------------------------+---------------------------------------------+----------+----------+----------+----------+----------+----------+

$ cat guava-gwt/sloc.txt
+--------------------+--------------------------------------+----------+----------+----------+----------+----------+----------+
| Short Package Name | File Name                            | Type     | Blank    | JavaDoc  | Comment  | Code     | Total    |
+--------------------+--------------------------------------+----------+----------+----------+----------+----------+----------+
| common             | DummyJavadocClass.java               | src      |        2 |        6 |       15 |        2 |       25 |
| common             | ForceGuavaCompilationEntryPoint.java | src      |        3 |        5 |       15 |        6 |       29 |
+--------------------+--------------------------------------+----------+----------+----------+----------+----------+----------+
| 1 package(s)       | 2 file(s)                            | java     |        5 |       11 |       30 |        8 |       54 |
+--------------------+--------------------------------------+----------+----------+----------+----------+----------+----------+

## delete sloc.txt files
$ git clean -n
Would remove guava-gwt/sloc.txt
Would remove guava-testlib/sloc.txt
Would remove guava/sloc.txt

$ git clean -f
Removing guava-gwt/sloc.txt
Removing guava-testlib/sloc.txt
Removing guava/sloc.txt

$ git status
On branch master
Your branch is up to date with 'origin/master'.

nothing to commit, working tree clean
```
