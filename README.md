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

By default the package names are trimmed by removing the package name prefix shared by the most packages, in order to prevent too much repetition. A package that shares no prefix with the others is displayed in full.

It can be tested right away on its own source code:

```
$ mvn io.github.orhankupusoglu:sloc-maven-plugin:sloc
[INFO] Scanning for projects...
[INFO] 
[INFO] -------------< io.github.orhankupusoglu:sloc-maven-plugin >-------------
[INFO] Building sloc-maven-plugin 1.1.0-SNAPSHOT
[INFO]   from pom.xml
[INFO] ----------------------------[ maven-plugin ]----------------------------
[INFO] 
[INFO] --- sloc:1.1.0-SNAPSHOT:sloc (default-cli) @ sloc-maven-plugin ---
[INFO] SLOC - directory: /home/orhanku/ME/Dev/sloc-maven-plugin/src
+------------------+--------------------+----------+----------+----------+----------+----------+----------+
| Package Name     | File Name          | Type     | Blank    | JavaDoc  | Comment  | Code     | Total    |
+------------------+--------------------+----------+----------+----------+----------+----------+----------+
| foo.bar          | HelloWorld.java    | test     |        4 |        3 |       10 |        9 |       26 |
+------------------+--------------------+----------+----------+----------+----------+----------+----------+
| engine           | Common.java        | src      |       52 |       13 |        7 |      159 |      231 |
| engine           | CommonTest.java    | test     |       41 |        0 |        4 |      164 |      209 |
| engine           | CountLines.java    | src      |       10 |        0 |        0 |       42 |       52 |
| engine           | CountSLOC.java     | src      |       34 |       12 |        2 |      182 |      230 |
| engine           | CountSLOCTest.java | test     |       18 |       20 |        2 |       71 |      111 |
+------------------+--------------------+----------+----------+----------+----------+----------+----------+
| goal             | GoalSLOC.java      | src      |       10 |       33 |        0 |       39 |       82 |
+------------------+--------------------+----------+----------+----------+----------+----------+----------+
| 3 package(s)     | 7 file(s)          | java     |      169 |       81 |       25 |      666 |      941 |
+------------------+--------------------+----------+----------+----------+----------+----------+----------+

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  0.206 s
[INFO] Finished at: 2026-11-16T20:02:18+01:00
[INFO] ------------------------------------------------------------------------
```

Note: the standalone test fixture `foo.bar` shares no prefix with the other packages, therefore it is displayed in full, while the plugin's own packages are trimmed to `engine` and `goal`.

```
## use untrimmed package names
$ mvn io.github.orhankupusoglu:sloc-maven-plugin:sloc -DtrimPkgNames=false
[INFO] Scanning for projects...
[INFO] 
[INFO] -------------< io.github.orhankupusoglu:sloc-maven-plugin >-------------
[INFO] Building sloc-maven-plugin 1.1.0-SNAPSHOT
[INFO]   from pom.xml
[INFO] ----------------------------[ maven-plugin ]----------------------------
[INFO] 
[INFO] --- sloc:1.1.0-SNAPSHOT:sloc (default-cli) @ sloc-maven-plugin ---
[INFO] SLOC - directory: /home/orhanku/ME/Dev/sloc-maven-plugin/src
+------------------------------------------+--------------------+----------+----------+----------+----------+----------+----------+
| Package Name                             | File Name          | Type     | Blank    | JavaDoc  | Comment  | Code     | Total    |
+------------------------------------------+--------------------+----------+----------+----------+----------+----------+----------+
| foo.bar                                  | HelloWorld.java    | test     |        4 |        3 |       10 |        9 |       26 |
+------------------------------------------+--------------------+----------+----------+----------+----------+----------+----------+
| kupusoglu.orhan.sloc_maven_plugin.engine | Common.java        | src      |       52 |       13 |        7 |      159 |      231 |
| kupusoglu.orhan.sloc_maven_plugin.engine | CommonTest.java    | test     |       41 |        0 |        4 |      164 |      209 |
| kupusoglu.orhan.sloc_maven_plugin.engine | CountLines.java    | src      |       10 |        0 |        0 |       42 |       52 |
| kupusoglu.orhan.sloc_maven_plugin.engine | CountSLOC.java     | src      |       34 |       12 |        2 |      182 |      230 |
| kupusoglu.orhan.sloc_maven_plugin.engine | CountSLOCTest.java | test     |       18 |       20 |        2 |       71 |      111 |
+------------------------------------------+--------------------+----------+----------+----------+----------+----------+----------+
| kupusoglu.orhan.sloc_maven_plugin.goal   | GoalSLOC.java      | src      |       10 |       33 |        0 |       39 |       82 |
+------------------------------------------+--------------------+----------+----------+----------+----------+----------+----------+
| 3 package(s)                             | 7 file(s)          | java     |      169 |       81 |       25 |      666 |      941 |
+------------------------------------------+--------------------+----------+----------+----------+----------+----------+----------+

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  0.212 s
[INFO] Finished at: 2026-11-16T20:02:19+01:00
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
+-------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| Package Name            | File Name                                  | Type     | Blank    | JavaDoc  | Comment  | Code     | Total    |
+-------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
|                         | module-info.java                           | src      |        2 |        1 |       15 |       24 |       42 |
+-------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| common.annotations      | Beta.java                                  | src      |        3 |       13 |       13 |       17 |       46 |
| common.annotations      | GwtCompatible.java                         | src      |        5 |       25 |       13 |       14 |       57 |
| common.annotations      | GwtIncompatible.java                       | src      |        3 |       18 |       13 |       13 |       47 |
| common.annotations      | J2ktIncompatible.java                      | src      |        3 |        6 |       13 |        9 |       31 |
| common.annotations      | VisibleForTesting.java                     | src      |        2 |       13 |       13 |        4 |       32 |
| common.annotations      | package-info.java                          | src      |        1 |        4 |       13 |        1 |       19 |
+-------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| common.base             | Absent.java                                | src      |       19 |        1 |       13 |       68 |      101 |
| common.base             | AbstractIterator.java                      | src      |       13 |        4 |       14 |       62 |       93 |
| common.base             | Ascii.java                                 | src      |       61 |      391 |       24 |      159 |      635 |
| common.base             | CaseFormat.java                            | src      |       29 |       27 |       15 |      150 |      221 |
| common.base             | CharMatcher.java                           | src      |      250 |      447 |       93 |     1046 |     1836 |
| common.base             | Charsets.java                              | src      |       10 |       43 |       13 |       20 |       86 |
| common.base             | CommonMatcher.java                         | src      |        8 |       17 |       13 |       11 |       49 |
| common.base             | CommonPattern.java                         | src      |        8 |        8 |       14 |       16 |       46 |
| common.base             | Converter.java                             | src      |       69 |      174 |      112 |      255 |      610 |
| common.base             | Defaults.java                              | src      |        6 |       11 |       18 |       39 |       74 |
| common.base             | Enums.java                                 | src      |       20 |       29 |       13 |       86 |      148 |
| common.base             | Equivalence.java                           | src      |       45 |      181 |       31 |      163 |      420 |
| common.base             | ExtraObjectsMethodsForWeb.java             | src      |        3 |        4 |       13 |        4 |       24 |
| common.base             | FinalizablePhantomReference.java           | src      |        3 |       17 |       13 |       15 |       48 |
| common.base             | FinalizableReference.java                  | src      |        3 |       12 |       13 |       10 |       38 |
| common.base             | FinalizableReferenceQueue.java             | src      |       34 |      143 |       76 |      186 |      439 |
| common.base             | FinalizableSoftReference.java              | src      |        3 |       14 |       13 |       15 |       45 |
| common.base             | FinalizableWeakReference.java              | src      |        3 |       14 |       13 |       15 |       45 |
| common.base             | Function.java                              | src      |        4 |       32 |       13 |       13 |       62 |
| common.base             | FunctionalEquivalence.java                 | src      |       12 |        6 |       13 |       46 |       77 |
| common.base             | Functions.java                             | src      |       61 |      116 |       18 |      255 |      450 |
| common.base             | IgnoreJRERequirement.java                  | src      |        4 |        5 |       13 |        8 |       30 |
| common.base             | Internal.java                              | src      |        5 |        8 |       16 |       18 |       47 |
| common.base             | Java8Compatibility.java                    | src      |        7 |        4 |       13 |       21 |       45 |
| common.base             | JdkPattern.java                            | src      |       17 |        1 |       13 |       60 |       91 |
| common.base             | Joiner.java                                | src      |       47 |      183 |       60 |      284 |      574 |
| common.base             | MoreObjects.java                           | src      |       38 |      218 |       16 |      206 |      478 |
| common.base             | NullnessCasts.java                         | src      |        4 |       31 |       13 |       12 |       60 |
| common.base             | Objects.java                               | src      |        6 |       47 |       13 |       16 |       82 |
| common.base             | Optional.java                              | src      |       24 |      251 |       13 |       75 |      363 |
| common.base             | PairwiseEquivalence.java                   | src      |       13 |        0 |       13 |       52 |       78 |
| common.base             | ParametricNullness.java                    | src      |        4 |       41 |       15 |       12 |       72 |
| common.base             | PatternCompiler.java                       | src      |        4 |       14 |       13 |       14 |       45 |
| common.base             | Platform.java                              | src      |       18 |       17 |       23 |       57 |      115 |
| common.base             | Preconditions.java                         | src      |       89 |      727 |       64 |      660 |     1540 |
| common.base             | Predicate.java                             | src      |        5 |       47 |       13 |       15 |       80 |
| common.base             | Predicates.java                            | src      |      102 |      230 |       28 |      445 |      805 |
| common.base             | Present.java                               | src      |       17 |        1 |       13 |       70 |      101 |
| common.base             | SmallCharMatcher.java                      | src      |       13 |       11 |       32 |       90 |      146 |
| common.base             | SneakyThrows.java                          | src      |        5 |       21 |       15 |       15 |       56 |
| common.base             | Splitter.java                              | src      |       55 |      275 |       31 |      257 |      618 |
| common.base             | StandardSystemProperty.java                | src      |       36 |       66 |       13 |       49 |      164 |
| common.base             | Stopwatch.java                             | src      |       22 |      124 |       14 |      126 |      286 |
| common.base             | Strings.java                               | src      |       24 |      125 |       19 |      151 |      319 |
| common.base             | Supplier.java                              | src      |        4 |       35 |       13 |       12 |       64 |
| common.base             | Suppliers.java                             | src      |       53 |       95 |       36 |      272 |      456 |
| common.base             | Throwables.java                            | src      |       35 |      238 |       40 |      244 |      557 |
| common.base             | Ticker.java                                | src      |        6 |       18 |       13 |       18 |       55 |
| common.base             | Utf8.java                                  | src      |       15 |       42 |       35 |      107 |      199 |
| common.base             | Verify.java                                | src      |       32 |      287 |       23 |      198 |      540 |
| common.base             | VerifyException.java                       | src      |        7 |       21 |       13 |       19 |       60 |
| common.base             | package-info.java                          | src      |        2 |       44 |       13 |        5 |       64 |
+-------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| common.base.internal    | Finalizer.java                             | src      |       26 |       43 |       51 |      145 |      265 |
+-------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| common.cache            | AbstractCache.java                         | src      |       31 |      105 |       18 |      137 |      291 |
| common.cache            | AbstractLoadingCache.java                  | src      |        8 |       15 |       13 |       42 |       78 |
| common.cache            | Cache.java                                 | src      |       15 |      119 |       18 |       33 |      185 |
| common.cache            | CacheBuilder.java                          | src      |       78 |      593 |       31 |      445 |     1147 |
| common.cache            | CacheBuilderSpec.java                      | src      |       46 |       85 |       15 |      353 |      499 |
| common.cache            | CacheLoader.java                           | src      |       22 |      121 |       17 |       91 |      251 |
| common.cache            | CacheStats.java                            | src      |       25 |      137 |       13 |      129 |      304 |
| common.cache            | ForwardingCache.java                       | src      |       20 |       34 |       18 |       76 |      148 |
| common.cache            | ForwardingLoadingCache.java                | src      |       13 |       19 |       13 |       49 |       94 |
| common.cache            | IgnoreJRERequirement.java                  | src      |        4 |        5 |       13 |        8 |       30 |
| common.cache            | LoadingCache.java                          | src      |        9 |      120 |       13 |       29 |      171 |
| common.cache            | LocalCache.java                            | src      |      718 |      347 |      277 |     3654 |     4996 |
| common.cache            | LongAddable.java                           | src      |        5 |        5 |       13 |        8 |       31 |
| common.cache            | LongAddables.java                          | src      |        5 |        5 |       13 |       11 |       34 |
| common.cache            | NullnessCasts.java                         | src      |        5 |       32 |       14 |       17 |       68 |
| common.cache            | ParametricNullness.java                    | src      |        4 |       41 |       15 |       12 |       72 |
| common.cache            | ReferenceEntry.java                        | src      |       21 |       37 |       23 |       30 |      111 |
| common.cache            | RemovalCause.java                          | src      |        8 |       34 |       13 |       39 |       94 |
| common.cache            | RemovalListener.java                       | src      |        3 |       22 |       15 |        7 |       47 |
| common.cache            | RemovalListeners.java                      | src      |        6 |       13 |       13 |       15 |       47 |
| common.cache            | RemovalNotification.java                   | src      |        9 |       23 |       13 |       27 |       72 |
| common.cache            | Weigher.java                               | src      |        4 |       12 |       13 |        7 |       36 |
| common.cache            | package-info.java                          | src      |        2 |       16 |       13 |        5 |       36 |
+-------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| common.collect          | AbstractBiMap.java                         | src      |       74 |       21 |       46 |      352 |      493 |
| common.collect          | AbstractIndexedListIterator.java           | src      |       13 |       25 |       15 |       54 |      107 |
| common.collect          | AbstractIterator.java                      | src      |       16 |       78 |       19 |       65 |      178 |
| common.collect          | AbstractListMultimap.java                  | src      |       16 |       53 |       16 |       62 |      147 |
| common.collect          | AbstractMapBasedMultimap.java              | src      |      230 |      160 |       80 |     1208 |     1678 |
| common.collect          | AbstractMapBasedMultiset.java              | src      |       44 |       23 |       39 |      247 |      353 |
| common.collect          | AbstractMapEntry.java                      | src      |        9 |       10 |       15 |       41 |       75 |
| common.collect          | AbstractMultimap.java                      | src      |       30 |       19 |       18 |      146 |      213 |
| common.collect          | AbstractMultiset.java                      | src      |       32 |       36 |       20 |      116 |      204 |
| common.collect          | AbstractNavigableMap.java                  | src      |       30 |        5 |       26 |      125 |      186 |
| common.collect          | AbstractRangeSet.java                      | src      |       17 |        5 |       13 |       66 |      101 |
| common.collect          | AbstractSequentialIterator.java            | src      |        7 |       28 |       15 |       25 |       75 |
| common.collect          | AbstractSetMultimap.java                   | src      |       17 |       58 |       16 |       66 |      157 |
| common.collect          | AbstractSortedKeySortedSetMultimap.java    | src      |        8 |        8 |       15 |       29 |       60 |
| common.collect          | AbstractSortedMultiset.java                | src      |       20 |        9 |       15 |      102 |      146 |
| common.collect          | AbstractSortedSetMultimap.java             | src      |       15 |       57 |       16 |       65 |      153 |
| common.collect          | AbstractTable.java                         | src      |       36 |        6 |       13 |      183 |      238 |
| common.collect          | AllEqualOrdering.java                      | src      |       11 |        5 |       15 |       38 |       69 |
| common.collect          | ArrayListMultimap.java                     | src      |       16 |       70 |       16 |       77 |      179 |
| common.collect          | ArrayTable.java                            | src      |       92 |      228 |       38 |      445 |      803 |
| common.collect          | BaseImmutableMultimap.java                 | src      |        2 |        4 |       15 |        4 |       25 |
| common.collect          | BiMap.java                                 | src      |       10 |       70 |       18 |       19 |      117 |
| common.collect          | BoundType.java                             | src      |        6 |        9 |       13 |       14 |       42 |
| common.collect          | ByFunctionOrdering.java                    | src      |       10 |        4 |       15 |       43 |       72 |
| common.collect          | CartesianList.java                         | src      |       17 |        5 |       14 |      134 |      170 |
| common.collect          | ClassToInstanceMap.java                    | src      |        4 |       38 |       15 |       15 |       72 |
| common.collect          | CollectCollectors.java                     | src      |       45 |        1 |       33 |      380 |      459 |
| common.collect          | CollectPreconditions.java                  | src      |       11 |        6 |       15 |       46 |       78 |
| common.collect          | CollectSpliterators.java                   | src      |       47 |       55 |       23 |      426 |      551 |
| common.collect          | Collections2.java                          | src      |       74 |      173 |       33 |      428 |      708 |
| common.collect          | CompactHashMap.java                        | src      |      128 |      134 |      115 |      826 |     1203 |
| common.collect          | CompactHashSet.java                        | src      |       82 |      135 |       36 |      504 |      757 |
| common.collect          | CompactHashing.java                        | src      |       24 |       42 |       18 |      115 |      199 |
| common.collect          | CompactLinkedHashMap.java                  | src      |       46 |       41 |       32 |      202 |      321 |
| common.collect          | CompactLinkedHashSet.java                  | src      |       41 |       55 |       33 |      154 |      283 |
| common.collect          | ComparatorOrdering.java                    | src      |       10 |        1 |       15 |       40 |       66 |
| common.collect          | Comparators.java                           | src      |       16 |      145 |       19 |       94 |      274 |
| common.collect          | ComparisonChain.java                       | src      |       37 |      139 |       15 |      117 |      308 |
| common.collect          | CompoundOrdering.java                      | src      |       10 |        1 |       15 |       52 |       78 |
| common.collect          | ComputationException.java                  | src      |        4 |       14 |       15 |       13 |       46 |
| common.collect          | ConcurrentHashMultiset.java                | src      |       57 |      125 |       78 |      370 |      630 |
| common.collect          | ConsumingQueueIterator.java                | src      |        6 |        4 |       14 |       19 |       43 |
| common.collect          | ContiguousSet.java                         | src      |       28 |      103 |       22 |      124 |      277 |
| common.collect          | Count.java                                 | src      |       12 |        5 |       13 |       40 |       70 |
| common.collect          | Cut.java                                   | src      |       86 |        9 |       36 |      345 |      476 |
| common.collect          | DenseImmutableTable.java                   | src      |       51 |        2 |       26 |      252 |      331 |
| common.collect          | DescendingImmutableSortedMultiset.java     | src      |       15 |        5 |       14 |       58 |       92 |
| common.collect          | DescendingImmutableSortedSet.java          | src      |       20 |        5 |       16 |       84 |      125 |
| common.collect          | DescendingMultiset.java                    | src      |       23 |        6 |       15 |      105 |      149 |
| common.collect          | DiscreteDomain.java                        | src      |       48 |       95 |       15 |      182 |      340 |
| common.collect          | EmptyContiguousSet.java                    | src      |       30 |        5 |       13 |      134 |      182 |
| common.collect          | EmptyImmutableListMultimap.java            | src      |        7 |        5 |       21 |       20 |       53 |
| common.collect          | EmptyImmutableSetMultimap.java             | src      |        7 |        5 |       21 |       20 |       53 |
| common.collect          | EnumBiMap.java                             | src      |       16 |       31 |       29 |       86 |      162 |
| common.collect          | EnumHashBiMap.java                         | src      |       15 |       31 |       24 |       69 |      139 |
| common.collect          | EnumMultiset.java                          | src      |       30 |       34 |       18 |      239 |      321 |
| common.collect          | EvictingQueue.java                         | src      |       15 |       39 |       25 |       66 |      145 |
| common.collect          | ExplicitOrdering.java                      | src      |       12 |        1 |       15 |       46 |       74 |
| common.collect          | FilteredEntryMultimap.java                 | src      |       57 |        6 |       22 |      377 |      462 |
| common.collect          | FilteredEntrySetMultimap.java              | src      |       12 |        5 |       15 |       45 |       77 |
| common.collect          | FilteredKeyListMultimap.java               | src      |        7 |        5 |       15 |       28 |       55 |
| common.collect          | FilteredKeyMultimap.java                   | src      |       33 |        5 |       14 |      177 |      229 |
| common.collect          | FilteredKeySetMultimap.java                | src      |       12 |        5 |       15 |       45 |       77 |
| common.collect          | FilteredMultimap.java                      | src      |        4 |        5 |       15 |       11 |       35 |
| common.collect          | FilteredMultimapValues.java                | src      |       12 |        5 |       13 |       64 |       94 |
| common.collect          | FilteredSetMultimap.java                   | src      |        3 |        5 |       15 |        9 |       32 |
| common.collect          | FluentIterable.java                        | src      |       52 |      527 |       26 |      254 |      859 |
| common.collect          | ForwardingBlockingDeque.java               | src      |       20 |       24 |       15 |       76 |      135 |
| common.collect          | ForwardingCollection.java                  | src      |       33 |      107 |       16 |      128 |      284 |
| common.collect          | ForwardingConcurrentMap.java               | src      |       13 |       24 |       15 |       44 |       96 |
| common.collect          | ForwardingDeque.java                       | src      |       22 |       18 |       15 |       97 |      152 |
| common.collect          | ForwardingImmutableList.java               | src      |        3 |        5 |       15 |        6 |       29 |
| common.collect          | ForwardingImmutableMap.java                | src      |        3 |        5 |       15 |        6 |       29 |
| common.collect          | ForwardingImmutableSet.java                | src      |        3 |        5 |       15 |        6 |       29 |
| common.collect          | ForwardingIterator.java                    | src      |       12 |       24 |       15 |       38 |       89 |
| common.collect          | ForwardingList.java                        | src      |       31 |      104 |       16 |      118 |      269 |
| common.collect          | ForwardingListIterator.java                | src      |       11 |       16 |       15 |       38 |       80 |
| common.collect          | ForwardingListMultimap.java                | src      |        8 |       13 |       15 |       26 |       62 |
| common.collect          | ForwardingMap.java                         | src      |       36 |      133 |       16 |      145 |      330 |
| common.collect          | ForwardingMapEntry.java                    | src      |       17 |       53 |       16 |       67 |      153 |
| common.collect          | ForwardingMultimap.java                    | src      |       25 |       13 |       16 |      102 |      156 |
| common.collect          | ForwardingMultiset.java                    | src      |       36 |      148 |       15 |      136 |      335 |
| common.collect          | ForwardingNavigableMap.java                | src      |       54 |      147 |       15 |      223 |      439 |
| common.collect          | ForwardingNavigableSet.java                | src      |       33 |       96 |       15 |      124 |      268 |
| common.collect          | ForwardingObject.java                      | src      |        7 |       32 |       16 |       12 |       67 |
| common.collect          | ForwardingQueue.java                       | src      |       17 |       49 |       15 |       71 |      152 |
| common.collect          | ForwardingSet.java                         | src      |       14 |       51 |       16 |       44 |      125 |
| common.collect          | ForwardingSetMultimap.java                 | src      |        8 |       13 |       15 |       31 |       67 |
| common.collect          | ForwardingSortedMap.java                   | src      |       19 |       56 |       22 |       83 |      180 |
| common.collect          | ForwardingSortedMultiset.java              | src      |       26 |       92 |       13 |      124 |      255 |
| common.collect          | ForwardingSortedSet.java                   | src      |       18 |       57 |       21 |       87 |      183 |
| common.collect          | ForwardingSortedSetMultimap.java           | src      |        9 |       13 |       15 |       29 |       66 |
| common.collect          | ForwardingTable.java                       | src      |       25 |        9 |       15 |      102 |      151 |
| common.collect          | GeneralRange.java                          | src      |       35 |       27 |       24 |      233 |      319 |
| common.collect          | HashBasedTable.java                        | src      |       12 |       38 |       15 |       43 |      108 |
| common.collect          | HashBiMap.java                             | src      |      124 |       53 |       25 |      773 |      975 |
| common.collect          | HashMultimap.java                          | src      |       14 |       62 |       15 |       64 |      155 |
| common.collect          | HashMultiset.java                          | src      |       12 |       26 |       15 |       46 |       99 |
| common.collect          | Hashing.java                               | src      |       10 |        7 |       30 |       30 |       77 |
| common.collect          | IgnoreJRERequirement.java                  | src      |        4 |        5 |       13 |        8 |       30 |
| common.collect          | ImmutableAsList.java                       | src      |       13 |        8 |       17 |       52 |       90 |
| common.collect          | ImmutableBiMap.java                        | src      |       47 |      253 |       32 |      345 |      677 |
| common.collect          | ImmutableClassToInstanceMap.java           | src      |       22 |       75 |       16 |       94 |      207 |
| common.collect          | ImmutableCollection.java                   | src      |       36 |      247 |       37 |      190 |      510 |
| common.collect          | ImmutableEnumMap.java                      | src      |       22 |        5 |       19 |      106 |      152 |
| common.collect          | ImmutableEnumSet.java                      | src      |       25 |        5 |       27 |      115 |      172 |
| common.collect          | ImmutableList.java                         | src      |       94 |      304 |       50 |      508 |      956 |
| common.collect          | ImmutableListMultimap.java                 | src      |       54 |      199 |       20 |      301 |      574 |
| common.collect          | ImmutableMap.java                          | src      |      119 |      379 |       91 |      755 |     1344 |
| common.collect          | ImmutableMapEntry.java                     | src      |       19 |       24 |       22 |       72 |      137 |
| common.collect          | ImmutableMapEntrySet.java                  | src      |       27 |        6 |       16 |      112 |      161 |
| common.collect          | ImmutableMapKeySet.java                    | src      |       17 |        6 |       17 |       66 |      106 |
| common.collect          | ImmutableMapValues.java                    | src      |       21 |        6 |       24 |       98 |      149 |
| common.collect          | ImmutableMultimap.java                     | src      |       95 |      236 |       32 |      478 |      841 |
| common.collect          | ImmutableMultiset.java                     | src      |       76 |      204 |       22 |      368 |      670 |
| common.collect          | ImmutableRangeMap.java                     | src      |       46 |       79 |       15 |      326 |      466 |
| common.collect          | ImmutableRangeSet.java                     | src      |       98 |      162 |       21 |      615 |      896 |
| common.collect          | ImmutableSet.java                          | src      |       92 |      203 |      108 |      541 |      944 |
| common.collect          | ImmutableSetMultimap.java                  | src      |       71 |      239 |       35 |      400 |      745 |
| common.collect          | ImmutableSortedAsList.java                 | src      |       14 |        6 |       26 |       59 |      105 |
| common.collect          | ImmutableSortedMap.java                    | src      |      113 |      508 |       59 |      798 |     1478 |
| common.collect          | ImmutableSortedMultiset.java               | src      |       59 |      348 |       19 |      343 |      769 |
| common.collect          | ImmutableSortedSet.java                    | src      |       86 |      432 |       58 |      458 |     1034 |
| common.collect          | ImmutableTable.java                        | src      |       49 |      170 |       15 |      268 |      502 |
| common.collect          | IndexedImmutableSet.java                   | src      |       12 |        0 |       17 |       60 |       89 |
| common.collect          | Internal.java                              | src      |        5 |        8 |       15 |       16 |       44 |
| common.collect          | Interner.java                              | src      |        3 |       23 |       15 |       10 |       51 |
| common.collect          | Interners.java                             | src      |       25 |       44 |       23 |      104 |      196 |
| common.collect          | Iterables.java                             | src      |       75 |      509 |       38 |      464 |     1086 |
| common.collect          | Iterators.java                             | src      |      123 |      480 |       62 |      807 |     1472 |
| common.collect          | JdkBackedImmutableBiMap.java               | src      |       20 |        4 |       18 |      117 |      159 |
| common.collect          | JdkBackedImmutableMap.java                 | src      |       14 |        9 |       21 |       97 |      141 |
| common.collect          | JdkBackedImmutableMultiset.java            | src      |       12 |        6 |       14 |       67 |       99 |
| common.collect          | JdkBackedImmutableSet.java                 | src      |        9 |        6 |       14 |       36 |       65 |
| common.collect          | LexicographicalOrdering.java               | src      |        9 |        1 |       15 |       54 |       79 |
| common.collect          | LinkedHashMultimap.java                    | src      |       73 |      126 |       42 |      466 |      707 |
| common.collect          | LinkedHashMultiset.java                    | src      |       12 |       33 |       15 |       47 |      107 |
| common.collect          | LinkedListMultimap.java                    | src      |       86 |      146 |       52 |      575 |      859 |
| common.collect          | ListMultimap.java                          | src      |        7 |       52 |       15 |       23 |       97 |
| common.collect          | Lists.java                                 | src      |      128 |      384 |       29 |      704 |     1245 |
| common.collect          | MapDifference.java                         | src      |       13 |       55 |       15 |       29 |      112 |
| common.collect          | MapMaker.java                              | src      |       25 |      141 |       16 |      132 |      314 |
| common.collect          | MapMakerInternalMap.java                   | src      |      411 |      300 |      138 |     2069 |     2918 |
| common.collect          | Maps.java                                  | src      |      488 |     1248 |      134 |     2629 |     4499 |
| common.collect          | MinMaxPriorityQueue.java                   | src      |       89 |      244 |       80 |      569 |      982 |
| common.collect          | MoreCollectors.java                        | src      |       18 |       24 |       21 |      122 |      185 |
| common.collect          | Multimap.java                              | src      |       29 |      290 |       20 |       51 |      390 |
| common.collect          | MultimapBuilder.java                       | src      |       61 |      139 |       25 |      275 |      500 |
| common.collect          | Multimaps.java                             | src      |      211 |      836 |       30 |     1130 |     2207 |
| common.collect          | Multiset.java                              | src      |       35 |      341 |       22 |       85 |      483 |
| common.collect          | Multisets.java                             | src      |      125 |      252 |       42 |      768 |     1187 |
| common.collect          | MutableClassToInstanceMap.java             | src      |       25 |       24 |       21 |      131 |      201 |
| common.collect          | NaturalOrdering.java                       | src      |       13 |        1 |       20 |       53 |       87 |
| common.collect          | NullnessCasts.java                         | src      |        5 |       32 |       14 |       17 |       68 |
| common.collect          | NullsFirstOrdering.java                    | src      |       12 |        1 |       16 |       62 |       91 |
| common.collect          | NullsLastOrdering.java                     | src      |       12 |        1 |       16 |       62 |       91 |
| common.collect          | ObjectArrays.java                          | src      |       19 |       90 |       21 |      112 |      242 |
| common.collect          | Ordering.java                              | src      |       66 |      585 |       65 |      298 |     1014 |
| common.collect          | ParametricNullness.java                    | src      |        4 |       41 |       15 |       12 |       72 |
| common.collect          | PeekingIterator.java                       | src      |        5 |       35 |       15 |       19 |       74 |
| common.collect          | Platform.java                              | src      |       17 |       34 |       27 |       58 |      136 |
| common.collect          | Queues.java                                | src      |       35 |      200 |       31 |      227 |      493 |
| common.collect          | Range.java                                 | src      |       58 |      364 |       30 |      281 |      733 |
| common.collect          | RangeMap.java                              | src      |       18 |      119 |       20 |       35 |      192 |
| common.collect          | RangeSet.java                              | src      |       29 |      179 |       18 |       51 |      277 |
| common.collect          | RegularContiguousSet.java                  | src      |       37 |        5 |       20 |      214 |      276 |
| common.collect          | RegularImmutableAsList.java                | src      |       15 |        6 |       16 |       64 |      101 |
| common.collect          | RegularImmutableBiMap.java                 | src      |       47 |       10 |       25 |      271 |      353 |
| common.collect          | RegularImmutableList.java                  | src      |       17 |        5 |       20 |       64 |      106 |
| common.collect          | RegularImmutableMap.java                   | src      |       49 |       53 |       63 |      284 |      449 |
| common.collect          | RegularImmutableMultiset.java              | src      |       23 |       19 |       14 |      146 |      202 |
| common.collect          | RegularImmutableSet.java                   | src      |       19 |        5 |       20 |       96 |      140 |
| common.collect          | RegularImmutableSortedMultiset.java        | src      |       21 |        5 |       14 |      102 |      142 |
| common.collect          | RegularImmutableSortedSet.java             | src      |       45 |        7 |       28 |      255 |      335 |
| common.collect          | RegularImmutableTable.java                 | src      |       25 |        9 |       29 |      145 |      208 |
| common.collect          | ReverseNaturalOrdering.java                | src      |       20 |        1 |       17 |       65 |      103 |
| common.collect          | ReverseOrdering.java                       | src      |       20 |        1 |       16 |       79 |      116 |
| common.collect          | RowSortedTable.java                        | src      |        4 |       22 |       15 |       16 |       57 |
| common.collect          | Serialization.java                         | src      |       12 |       54 |       15 |       94 |      175 |
| common.collect          | SetMultimap.java                           | src      |        8 |       72 |       15 |       26 |      121 |
| common.collect          | Sets.java                                  | src      |      219 |      766 |       69 |     1289 |     2343 |
| common.collect          | SingletonImmutableBiMap.java               | src      |       17 |        7 |       17 |       67 |      108 |
| common.collect          | SingletonImmutableList.java                | src      |       14 |        5 |       16 |       54 |       89 |
| common.collect          | SingletonImmutableSet.java                 | src      |       15 |        6 |       18 |       55 |       94 |
| common.collect          | SingletonImmutableTable.java               | src      |       13 |        5 |       15 |       53 |       86 |
| common.collect          | SneakyThrows.java                          | src      |        5 |       21 |       15 |       15 |       56 |
| common.collect          | SortedIterable.java                        | src      |        4 |       14 |       13 |       11 |       42 |
| common.collect          | SortedIterables.java                       | src      |        6 |        9 |       14 |       32 |       61 |
| common.collect          | SortedLists.java                           | src      |       14 |       94 |       21 |      175 |      304 |
| common.collect          | SortedMapDifference.java                   | src      |        7 |        6 |       15 |       16 |       44 |
| common.collect          | SortedMultiset.java                        | src      |       14 |       88 |       15 |       32 |      149 |
| common.collect          | SortedMultisetBridge.java                  | src      |        3 |        7 |       15 |        9 |       34 |
| common.collect          | SortedMultisets.java                       | src      |       28 |        7 |       15 |      129 |      179 |
| common.collect          | SortedSetMultimap.java                     | src      |        8 |       68 |       16 |       25 |      117 |
| common.collect          | SparseImmutableTable.java                  | src      |       16 |        1 |       23 |      104 |      144 |
| common.collect          | StandardRowSortedTable.java                | src      |       17 |       29 |       20 |       73 |      139 |
| common.collect          | StandardTable.java                         | src      |      120 |       54 |       77 |      763 |     1014 |
| common.collect          | Streams.java                               | src      |       73 |      312 |       48 |      554 |      987 |
| common.collect          | Synchronized.java                          | src      |      332 |       12 |       45 |     1850 |     2239 |
| common.collect          | Table.java                                 | src      |       32 |      184 |       19 |       60 |      295 |
| common.collect          | TableCollectors.java                       | src      |       23 |        1 |       25 |      175 |      224 |
| common.collect          | Tables.java                                | src      |       87 |      169 |       26 |      447 |      729 |
| common.collect          | TopKSelector.java                          | src      |       26 |       88 |       33 |      150 |      297 |
| common.collect          | TransformedIterator.java                   | src      |        9 |        6 |       15 |       28 |       58 |
| common.collect          | TransformedListIterator.java               | src      |       10 |        6 |       15 |       40 |       71 |
| common.collect          | TreeBasedTable.java                        | src      |       42 |       75 |       25 |      199 |      341 |
| common.collect          | TreeMultimap.java                          | src      |       19 |       90 |       15 |       99 |      223 |
| common.collect          | TreeMultiset.java                          | src      |      112 |       62 |       68 |      862 |     1104 |
| common.collect          | TreeRangeMap.java                          | src      |      103 |       20 |       44 |      663 |      830 |
| common.collect          | TreeRangeSet.java                          | src      |       96 |       34 |       62 |      731 |      923 |
| common.collect          | TreeTraverser.java                         | src      |       28 |       84 |       17 |      167 |      296 |
| common.collect          | UnmodifiableIterator.java                  | src      |        4 |       17 |       15 |       15 |       51 |
| common.collect          | UnmodifiableListIterator.java              | src      |        5 |       19 |       15 |       22 |       61 |
| common.collect          | UnmodifiableSortedMultiset.java            | src      |       17 |        7 |       19 |       74 |      117 |
| common.collect          | UsingToStringOrdering.java                 | src      |        8 |        1 |       16 |       22 |       47 |
| common.collect          | package-info.java                          | src      |        2 |      105 |       15 |        5 |      127 |
+-------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| common.escape           | ArrayBasedCharEscaper.java                 | src      |       11 |       63 |       24 |       52 |      150 |
| common.escape           | ArrayBasedEscaperMap.java                  | src      |        9 |       20 |       20 |       34 |       83 |
| common.escape           | ArrayBasedUnicodeEscaper.java              | src      |       12 |       66 |       42 |       85 |      205 |
| common.escape           | CharEscaper.java                           | src      |       20 |       59 |       26 |       75 |      180 |
| common.escape           | CharEscaperBuilder.java                    | src      |       14 |       29 |       19 |       65 |      127 |
| common.escape           | Escaper.java                               | src      |        6 |       59 |       14 |       15 |       94 |
| common.escape           | Escapers.java                              | src      |       18 |       84 |       18 |       71 |      191 |
| common.escape           | ParametricNullness.java                    | src      |        4 |       41 |       15 |       12 |       72 |
| common.escape           | Platform.java                              | src      |        6 |       11 |       14 |       17 |       48 |
| common.escape           | UnicodeEscaper.java                        | src      |       17 |      141 |       26 |      128 |      312 |
| common.escape           | package-info.java                          | src      |        2 |       12 |       13 |        5 |       32 |
+-------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| common.eventbus         | AllowConcurrentEvents.java                 | src      |        3 |        9 |       13 |        8 |       33 |
| common.eventbus         | AsyncEventBus.java                         | src      |        6 |       30 |       13 |       13 |       62 |
| common.eventbus         | DeadEvent.java                             | src      |        9 |       27 |       13 |       21 |       70 |
| common.eventbus         | Dispatcher.java                            | src      |       25 |       38 |       32 |       98 |      193 |
| common.eventbus         | EventBus.java                              | src      |       22 |      167 |       15 |       99 |      303 |
| common.eventbus         | ParametricNullness.java                    | src      |        4 |       41 |       15 |       12 |       72 |
| common.eventbus         | Subscribe.java                             | src      |        3 |       14 |       13 |       10 |       40 |
| common.eventbus         | Subscriber.java                            | src      |       20 |       28 |       16 |       81 |      145 |
| common.eventbus         | SubscriberExceptionContext.java            | src      |        9 |       19 |       13 |       28 |       69 |
| common.eventbus         | SubscriberExceptionHandler.java            | src      |        2 |        6 |       13 |        4 |       25 |
| common.eventbus         | SubscriberRegistry.java                    | src      |       34 |       32 |       36 |      165 |      267 |
| common.eventbus         | package-info.java                          | src      |        2 |        7 |       13 |        5 |       27 |
+-------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| common.graph            | AbstractBaseGraph.java                     | src      |       37 |       26 |       23 |      257 |      343 |
| common.graph            | AbstractDirectedNetworkConnections.java    | src      |       20 |        9 |       20 |       94 |      143 |
| common.graph            | AbstractGraph.java                         | src      |        7 |       10 |       15 |       33 |       65 |
| common.graph            | AbstractGraphBuilder.java                  | src      |        5 |       11 |       15 |       12 |       43 |
| common.graph            | AbstractNetwork.java                       | src      |       40 |       36 |       21 |      233 |      330 |
| common.graph            | AbstractUndirectedNetworkConnections.java  | src      |       15 |        8 |       17 |       66 |      106 |
| common.graph            | AbstractValueGraph.java                    | src      |       23 |       14 |       16 |      106 |      159 |
| common.graph            | ArchetypeGraph.java                        | src      |       17 |      132 |       24 |       18 |      191 |
| common.graph            | BaseGraph.java                             | src      |        8 |       46 |       21 |        8 |       83 |
| common.graph            | DirectedGraphConnections.java              | src      |       66 |       26 |       31 |      435 |      558 |
| common.graph            | DirectedMultiNetworkConnections.java       | src      |       19 |        7 |       15 |      104 |      145 |
| common.graph            | DirectedNetworkConnections.java            | src      |       10 |        7 |       15 |       34 |       66 |
| common.graph            | EdgesConnecting.java                       | src      |       10 |        9 |       15 |       35 |       69 |
| common.graph            | ElementOrder.java                          | src      |       20 |       77 |       15 |       94 |      206 |
| common.graph            | EndpointPair.java                          | src      |       36 |       54 |       25 |      136 |      251 |
| common.graph            | EndpointPairIterator.java                  | src      |       14 |       39 |       23 |       71 |      147 |
| common.graph            | ForwardingGraph.java                       | src      |       19 |       10 |       15 |       65 |      109 |
| common.graph            | ForwardingNetwork.java                     | src      |       30 |        7 |       15 |      111 |      163 |
| common.graph            | ForwardingValueGraph.java                  | src      |       22 |       11 |       15 |       79 |      127 |
| common.graph            | Graph.java                                 | src      |       23 |      257 |       27 |       42 |      349 |
| common.graph            | GraphBuilder.java                          | src      |       16 |       95 |       15 |       69 |      195 |
| common.graph            | GraphConnections.java                      | src      |       12 |       31 |       15 |       18 |       76 |
| common.graph            | GraphConstants.java                        | src      |        8 |        2 |       17 |       38 |       65 |
| common.graph            | Graphs.java                                | src      |       90 |      145 |       33 |      450 |      718 |
| common.graph            | GraphsBridgeMethods.java                   | src      |        4 |        4 |        0 |       12 |       20 |
| common.graph            | ImmutableGraph.java                        | src      |       18 |       83 |       20 |       80 |      201 |
| common.graph            | ImmutableNetwork.java                      | src      |       21 |      103 |       21 |       95 |      240 |
| common.graph            | ImmutableValueGraph.java                   | src      |       18 |       88 |       21 |       78 |      205 |
| common.graph            | IncidentEdgeSet.java                       | src      |       13 |       12 |       15 |       70 |      110 |
| common.graph            | InvalidatableSet.java                      | src      |        8 |        4 |        6 |       35 |       53 |
| common.graph            | MapIteratorCache.java                      | src      |       21 |       14 |       27 |       89 |      151 |
| common.graph            | MapRetrievalCache.java                     | src      |       16 |        6 |       23 |       65 |      110 |
| common.graph            | MultiEdgesConnecting.java                  | src      |        8 |        9 |       15 |       37 |       69 |
| common.graph            | MutableGraph.java                          | src      |        9 |       66 |       15 |       16 |      106 |
| common.graph            | MutableNetwork.java                        | src      |        8 |       74 |       15 |       14 |      111 |
| common.graph            | MutableValueGraph.java                     | src      |        9 |       74 |       15 |       17 |      115 |
| common.graph            | Network.java                               | src      |       28 |      364 |       27 |       37 |      456 |
| common.graph            | NetworkBuilder.java                        | src      |       17 |      101 |       15 |       67 |      200 |
| common.graph            | NetworkConnections.java                    | src      |       15 |       27 |       15 |       20 |       77 |
| common.graph            | ParametricNullness.java                    | src      |        4 |       41 |       15 |       12 |       72 |
| common.graph            | PredecessorsFunction.java                  | src      |        4 |       78 |       15 |        6 |      103 |
| common.graph            | StandardMutableGraph.java                  | src      |       11 |       11 |       15 |       38 |       75 |
| common.graph            | StandardMutableNetwork.java                | src      |       20 |       19 |       18 |      118 |      175 |
| common.graph            | StandardMutableValueGraph.java             | src      |       24 |       19 |       20 |      129 |      192 |
| common.graph            | StandardNetwork.java                       | src      |       27 |       24 |       20 |      137 |      208 |
| common.graph            | StandardValueGraph.java                    | src      |       25 |       22 |       17 |      124 |      188 |
| common.graph            | SuccessorsFunction.java                    | src      |        4 |       81 |       15 |        6 |      106 |
| common.graph            | Traverser.java                             | src      |       26 |      269 |       26 |      175 |      496 |
| common.graph            | UndirectedGraphConnections.java            | src      |       16 |        7 |       15 |       82 |      120 |
| common.graph            | UndirectedMultiNetworkConnections.java     | src      |       16 |        7 |       15 |       87 |      125 |
| common.graph            | UndirectedNetworkConnections.java          | src      |        9 |        7 |       15 |       27 |       58 |
| common.graph            | ValueGraph.java                            | src      |       28 |      301 |       27 |       46 |      402 |
| common.graph            | ValueGraphBuilder.java                     | src      |       16 |      104 |       15 |       68 |      203 |
| common.graph            | package-info.java                          | src      |        2 |        7 |       15 |        5 |       29 |
+-------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| common.hash             | AbstractByteHasher.java                    | src      |       18 |       11 |       13 |      103 |      145 |
| common.hash             | AbstractCompositeHashFunction.java         | src      |       25 |       12 |       14 |      148 |      199 |
| common.hash             | AbstractHashFunction.java                  | src      |       12 |        5 |       13 |       51 |       81 |
| common.hash             | AbstractHasher.java                        | src      |       15 |        6 |       13 |      101 |      135 |
| common.hash             | AbstractNonStreamingHashFunction.java      | src      |       27 |       12 |       15 |      117 |      171 |
| common.hash             | AbstractStreamingHasher.java               | src      |       27 |       38 |       34 |      124 |      223 |
| common.hash             | BloomFilter.java                           | src      |       54 |      320 |       54 |      300 |      728 |
| common.hash             | BloomFilterStrategies.java                 | src      |       30 |       58 |       23 |      205 |      316 |
| common.hash             | ChecksumHashFunction.java                  | src      |       19 |        6 |       21 |      105 |      151 |
| common.hash             | Crc32cHashFunction.java                    | src      |       20 |        6 |       38 |      311 |      375 |
| common.hash             | FarmHashFingerprint64.java                 | src      |       50 |       17 |       38 |      463 |      568 |
| common.hash             | Fingerprint2011.java                       | src      |       24 |       24 |        9 |      142 |      199 |
| common.hash             | Funnel.java                                | src      |        4 |       29 |       13 |       10 |       56 |
| common.hash             | Funnels.java                               | src      |       47 |       46 |       13 |      175 |      281 |
| common.hash             | HashCode.java                              | src      |       56 |      105 |       19 |      242 |      422 |
| common.hash             | HashFunction.java                          | src      |       13 |      174 |       13 |       21 |      221 |
| common.hash             | Hasher.java                                | src      |       18 |       68 |       13 |       55 |      154 |
| common.hash             | Hashing.java                               | src      |       65 |      393 |       20 |      364 |      842 |
| common.hash             | HashingInputStream.java                    | src      |       12 |       35 |       13 |       52 |      112 |
| common.hash             | HashingOutputStream.java                   | src      |        9 |       16 |       20 |       33 |       78 |
| common.hash             | IgnoreJRERequirement.java                  | src      |        4 |        5 |       13 |        8 |       30 |
| common.hash             | ImmutableSupplier.java                     | src      |        3 |        4 |       14 |        7 |       28 |
| common.hash             | Java8Compatibility.java                    | src      |        7 |        4 |       13 |       19 |       43 |
| common.hash             | LittleEndianByteArray.java                 | src      |       36 |       75 |       47 |      199 |      357 |
| common.hash             | MacHashFunction.java                       | src      |       21 |        6 |       14 |      102 |      143 |
| common.hash             | MessageDigestHashFunction.java             | src      |       26 |        7 |       14 |      126 |      173 |
| common.hash             | Murmur3_128HashFunction.java               | src      |       31 |        7 |       23 |      157 |      218 |
| common.hash             | Murmur3_32HashFunction.java                | src      |       55 |        9 |       34 |      333 |      431 |
| common.hash             | ParametricNullness.java                    | src      |        4 |       41 |       15 |       12 |       72 |
| common.hash             | Platform.java                              | src      |        4 |        1 |       13 |        8 |       26 |
| common.hash             | PrimitiveSink.java                         | src      |       15 |       62 |       13 |       34 |      124 |
| common.hash             | SipHashFunction.java                       | src      |       26 |       13 |       32 |      115 |      186 |
| common.hash             | SneakyThrows.java                          | src      |        5 |       21 |       15 |       15 |       56 |
| common.hash             | package-info.java                          | src      |        2 |        6 |       14 |        5 |       27 |
+-------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| common.html             | HtmlEscapers.java                          | src      |        6 |       29 |       16 |       19 |       70 |
| common.html             | ParametricNullness.java                    | src      |        4 |       41 |       15 |       12 |       72 |
| common.html             | package-info.java                          | src      |        2 |        8 |       13 |        5 |       28 |
+-------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| common.io               | AppendableWriter.java                      | src      |       17 |       13 |       22 |       77 |      129 |
| common.io               | BaseEncoding.java                          | src      |      141 |      241 |       34 |      871 |     1287 |
| common.io               | ByteArrayDataInput.java                    | src      |       17 |       15 |       15 |       53 |      100 |
| common.io               | ByteArrayDataOutput.java                   | src      |       17 |       15 |       13 |       39 |       84 |
| common.io               | ByteProcessor.java                         | src      |        4 |       19 |       13 |       16 |       52 |
| common.io               | ByteSink.java                              | src      |       17 |       60 |       13 |       54 |      144 |
| common.io               | ByteSource.java                            | src      |       78 |      218 |       38 |      413 |      747 |
| common.io               | ByteStreams.java                           | src      |       97 |      220 |       45 |      635 |      997 |
| common.io               | CharSequenceReader.java                    | src      |       20 |        7 |       25 |      102 |      154 |
| common.io               | CharSink.java                              | src      |       17 |       84 |       13 |       61 |      175 |
| common.io               | CharSource.java                            | src      |       68 |      282 |       19 |      371 |      740 |
| common.io               | CharStreams.java                           | src      |       34 |      134 |       20 |      180 |      368 |
| common.io               | Closeables.java                            | src      |        7 |       64 |       22 |       47 |      140 |
| common.io               | Closer.java                                | src      |       17 |      114 |       24 |      100 |      255 |
| common.io               | CountingInputStream.java                   | src      |       15 |       12 |       14 |       63 |      104 |
| common.io               | CountingOutputStream.java                  | src      |       10 |       12 |       16 |       32 |       70 |
| common.io               | FileBackedOutputStream.java                | src      |       57 |       68 |       46 |      322 |      493 |
| common.io               | FileWriteMode.java                         | src      |        3 |        7 |       13 |        8 |       31 |
| common.io               | Files.java                                 | src      |       68 |      411 |       31 |      393 |      903 |
| common.io               | Flushables.java                            | src      |        6 |       25 |       13 |       34 |       78 |
| common.io               | IgnoreJRERequirement.java                  | src      |        4 |        5 |       13 |        8 |       30 |
| common.io               | InsecureRecursiveDeleteException.java      | src      |        5 |       13 |       15 |       16 |       49 |
| common.io               | Java8Compatibility.java                    | src      |        9 |        4 |       13 |       27 |       53 |
| common.io               | LineBuffer.java                            | src      |       11 |       37 |       15 |       55 |      118 |
| common.io               | LineProcessor.java                         | src      |        5 |       16 |       13 |       14 |       48 |
| common.io               | LineReader.java                            | src      |        7 |       18 |       15 |       45 |       85 |
| common.io               | LittleEndianDataInputStream.java           | src      |       28 |       88 |       13 |      123 |      252 |
| common.io               | LittleEndianDataOutputStream.java          | src      |       17 |       62 |       17 |       75 |      171 |
| common.io               | MoreFiles.java                             | src      |       75 |      224 |      121 |      442 |      862 |
| common.io               | MultiInputStream.java                      | src      |       13 |       13 |       13 |       82 |      121 |
| common.io               | MultiReader.java                           | src      |       10 |        7 |       13 |       65 |       95 |
| common.io               | ParametricNullness.java                    | src      |        4 |       41 |       15 |       12 |       72 |
| common.io               | PatternFilenameFilter.java                 | src      |        7 |       18 |       26 |       23 |       74 |
| common.io               | ReaderInputStream.java                     | src      |       27 |       53 |       37 |      143 |      260 |
| common.io               | RecursiveDeleteOption.java                 | src      |        3 |       19 |       15 |       11 |       48 |
| common.io               | Resources.java                             | src      |       20 |       86 |       17 |       89 |      212 |
| common.io               | TempFileCreator.java                       | src      |       33 |       19 |       51 |      253 |      356 |
| common.io               | package-info.java                          | src      |        2 |       15 |       13 |        5 |       35 |
+-------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| common.math             | BigDecimalMath.java                        | src      |       14 |       76 |       17 |       70 |      177 |
| common.math             | BigIntegerMath.java                        | src      |       49 |       96 |       81 |      295 |      521 |
| common.math             | DoubleMath.java                            | src      |       33 |      191 |       43 |      265 |      532 |
| common.math             | DoubleUtils.java                           | src      |       18 |        7 |       42 |       64 |      131 |
| common.math             | IgnoreJRERequirement.java                  | src      |        4 |        5 |       13 |        8 |       30 |
| common.math             | IntMath.java                               | src      |       47 |      222 |       87 |      408 |      764 |
| common.math             | LinearTransformation.java                  | src      |       50 |       74 |       13 |      181 |      318 |
| common.math             | LongMath.java                              | src      |       78 |      270 |      194 |      831 |     1373 |
| common.math             | MathPreconditions.java                     | src      |       14 |        5 |       13 |       79 |      111 |
| common.math             | PairedStats.java                           | src      |       24 |      131 |       16 |      149 |      320 |
| common.math             | PairedStatsAccumulator.java                | src      |       18 |       91 |       31 |      104 |      244 |
| common.math             | ParametricNullness.java                    | src      |        4 |       41 |       15 |       12 |       72 |
| common.math             | Quantiles.java                             | src      |       54 |      295 |       66 |      290 |      705 |
| common.math             | Stats.java                                 | src      |       40 |      337 |       18 |      268 |      663 |
| common.math             | StatsAccumulator.java                      | src      |       29 |      194 |       34 |      173 |      430 |
| common.math             | ToDoubleRounder.java                       | src      |       10 |       12 |       16 |      110 |      148 |
| common.math             | package-info.java                          | src      |        2 |       10 |       13 |        5 |       30 |
+-------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| common.net              | HostAndPort.java                           | src      |       27 |      121 |       16 |      153 |      317 |
| common.net              | HostSpecifier.java                         | src      |       20 |       60 |       25 |       72 |      177 |
| common.net              | HttpHeaders.java                           | src      |      185 |      711 |       17 |      190 |     1103 |
| common.net              | InetAddresses.java                         | src      |      102 |      477 |       58 |      607 |     1244 |
| common.net              | InternetDomainName.java                    | src      |       68 |      336 |       41 |      201 |      646 |
| common.net              | MediaType.java                             | src      |      180 |      703 |       42 |      500 |     1425 |
| common.net              | ParametricNullness.java                    | src      |        4 |       41 |       15 |       12 |       72 |
| common.net              | PercentEscaper.java                        | src      |       13 |       54 |       39 |      127 |      233 |
| common.net              | UrlEscapers.java                           | src      |       12 |       95 |       15 |       27 |      149 |
| common.net              | package-info.java                          | src      |        2 |        8 |       13 |        5 |       28 |
+-------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| common.primitives       | Booleans.java                              | src      |       56 |      235 |       23 |      340 |      654 |
| common.primitives       | Bytes.java                                 | src      |       41 |      171 |       23 |      269 |      504 |
| common.primitives       | Chars.java                                 | src      |       59 |      292 |       22 |      372 |      745 |
| common.primitives       | Doubles.java                               | src      |       67 |      325 |       33 |      413 |      838 |
| common.primitives       | DoublesMethodsForWeb.java                  | src      |        3 |        4 |       13 |        4 |       24 |
| common.primitives       | Floats.java                                | src      |       64 |      316 |       28 |      384 |      792 |
| common.primitives       | FloatsMethodsForWeb.java                   | src      |        3 |        4 |       13 |        4 |       24 |
| common.primitives       | IgnoreJRERequirement.java                  | src      |        4 |        5 |       13 |        8 |       30 |
| common.primitives       | ImmutableDoubleArray.java                  | src      |       73 |      194 |       41 |      351 |      659 |
| common.primitives       | ImmutableIntArray.java                     | src      |       72 |      192 |       40 |      345 |      649 |
| common.primitives       | ImmutableLongArray.java                    | src      |       72 |      192 |       40 |      347 |      651 |
| common.primitives       | Ints.java                                  | src      |       70 |      351 |       48 |      414 |      883 |
| common.primitives       | IntsMethodsForWeb.java                     | src      |        3 |        4 |       13 |        4 |       24 |
| common.primitives       | Longs.java                                 | src      |       75 |      334 |       28 |      454 |      891 |
| common.primitives       | ParametricNullness.java                    | src      |        4 |       41 |       15 |       12 |       72 |
| common.primitives       | ParseRequest.java                          | src      |        7 |        1 |       14 |       33 |       55 |
| common.primitives       | Primitives.java                            | src      |       18 |       47 |       18 |       59 |      142 |
| common.primitives       | Shorts.java                                | src      |       67 |      305 |       22 |      399 |      793 |
| common.primitives       | ShortsMethodsForWeb.java                   | src      |        3 |        4 |       13 |        4 |       24 |
| common.primitives       | SignedBytes.java                           | src      |       18 |       91 |       16 |       93 |      218 |
| common.primitives       | UnsignedBytes.java                         | src      |       58 |      177 |       71 |      317 |      623 |
| common.primitives       | UnsignedInteger.java                       | src      |       29 |      110 |       15 |      106 |      260 |
| common.primitives       | UnsignedInts.java                          | src      |       30 |      190 |       14 |      160 |      394 |
| common.primitives       | UnsignedLong.java                          | src      |       31 |      108 |       21 |      114 |      274 |
| common.primitives       | UnsignedLongs.java                         | src      |       38 |      184 |       42 |      240 |      504 |
| common.primitives       | package-info.java                          | src      |        2 |       51 |       13 |        5 |       71 |
+-------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| common.reflect          | AbstractInvocationHandler.java             | src      |       10 |       61 |       18 |       57 |      146 |
| common.reflect          | ClassPath.java                             | src      |       59 |      219 |       29 |      404 |      711 |
| common.reflect          | IgnoreJRERequirement.java                  | src      |        4 |        5 |       13 |        8 |       30 |
| common.reflect          | ImmutableTypeToInstanceMap.java            | src      |       20 |       62 |       13 |       81 |      176 |
| common.reflect          | Invokable.java                             | src      |       78 |      106 |       32 |      340 |      556 |
| common.reflect          | MutableTypeToInstanceMap.java              | src      |       25 |       19 |       19 |      113 |      176 |
| common.reflect          | Parameter.java                             | src      |       20 |       32 |       13 |       95 |      160 |
| common.reflect          | ParametricNullness.java                    | src      |        4 |       41 |       15 |       12 |       72 |
| common.reflect          | Reflection.java                            | src      |        9 |       35 |       13 |       32 |       89 |
| common.reflect          | TypeCapture.java                           | src      |        5 |        6 |       13 |       11 |       35 |
| common.reflect          | TypeParameter.java                         | src      |        9 |       14 |       23 |       32 |       78 |
| common.reflect          | TypeResolver.java                          | src      |       56 |       89 |       89 |      392 |      626 |
| common.reflect          | TypeToInstanceMap.java                     | src      |        7 |       49 |       13 |       16 |       85 |
| common.reflect          | TypeToken.java                             | src      |      138 |      339 |      107 |      891 |     1475 |
| common.reflect          | TypeVisitor.java                           | src      |       10 |       35 |       14 |       45 |      104 |
| common.reflect          | Types.java                                 | src      |       87 |       65 |       38 |      528 |      718 |
| common.reflect          | package-info.java                          | src      |        2 |        4 |       13 |        5 |       24 |
+-------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| common.util.concurrent  | AbstractCatchingFuture.java                | src      |       23 |       11 |       27 |      187 |      248 |
| common.util.concurrent  | AbstractExecutionThreadService.java        | src      |       27 |       93 |       17 |      123 |      260 |
| common.util.concurrent  | AbstractFuture.java                        | src      |       65 |      227 |      184 |      582 |     1058 |
| common.util.concurrent  | AbstractFutureState.java                   | src      |       86 |       81 |      126 |      493 |      786 |
| common.util.concurrent  | AbstractIdleService.java                   | src      |       28 |       54 |       15 |      124 |      221 |
| common.util.concurrent  | AbstractListeningExecutorService.java      | src      |        8 |       18 |       13 |       41 |       80 |
| common.util.concurrent  | AbstractScheduledService.java              | src      |       69 |      227 |       97 |      366 |      759 |
| common.util.concurrent  | AbstractService.java                       | src      |       63 |      114 |       31 |      441 |      649 |
| common.util.concurrent  | AbstractTransformFuture.java               | src      |       22 |       11 |       75 |      155 |      263 |
| common.util.concurrent  | AggregateFuture.java                       | src      |       31 |       43 |      116 |      191 |      381 |
| common.util.concurrent  | AggregateFutureState.java                  | src      |       15 |       10 |       65 |       44 |      134 |
| common.util.concurrent  | AsyncCallable.java                         | src      |        3 |       16 |       13 |        9 |       41 |
| common.util.concurrent  | AsyncFunction.java                         | src      |        3 |       14 |       13 |        9 |       39 |
| common.util.concurrent  | AtomicDouble.java                          | src      |       30 |      146 |       13 |      120 |      309 |
| common.util.concurrent  | AtomicDoubleArray.java                     | src      |       28 |      155 |       16 |      131 |      330 |
| common.util.concurrent  | AtomicLongMap.java                         | src      |       36 |      140 |       15 |      166 |      357 |
| common.util.concurrent  | Atomics.java                               | src      |        7 |       55 |       13 |       35 |      110 |
| common.util.concurrent  | Callables.java                             | src      |        9 |       32 |       17 |       70 |      128 |
| common.util.concurrent  | ClosingFuture.java                         | src      |      151 |     1161 |       39 |     1090 |     2441 |
| common.util.concurrent  | CollectionFuture.java                      | src      |       16 |        3 |       20 |       73 |      112 |
| common.util.concurrent  | CombinedFuture.java                        | src      |       29 |        1 |       36 |      143 |      209 |
| common.util.concurrent  | CycleDetectingLockFactory.java             | src      |       94 |      342 |       68 |      474 |      978 |
| common.util.concurrent  | DirectExecutor.java                        | src      |        5 |        4 |       13 |       15 |       37 |
| common.util.concurrent  | DirectExecutorService.java                 | src      |       15 |        8 |       21 |       84 |      128 |
| common.util.concurrent  | ExecutionError.java                        | src      |        8 |       38 |       22 |       22 |       90 |
| common.util.concurrent  | ExecutionList.java                         | src      |       15 |       45 |       34 |       76 |      170 |
| common.util.concurrent  | ExecutionSequencer.java                    | src      |       32 |      129 |      139 |      178 |      478 |
| common.util.concurrent  | FakeTimeLimiter.java                       | src      |        9 |       11 |       13 |       67 |      100 |
| common.util.concurrent  | FluentFuture.java                          | src      |       20 |      288 |       13 |      105 |      426 |
| common.util.concurrent  | ForwardingBlockingDeque.java               | src      |       20 |       22 |       15 |       76 |      133 |
| common.util.concurrent  | ForwardingBlockingQueue.java               | src      |       12 |       15 |       13 |       50 |       90 |
| common.util.concurrent  | ForwardingCondition.java                   | src      |       10 |        1 |       13 |       40 |       64 |
| common.util.concurrent  | ForwardingExecutorService.java             | src      |       17 |       13 |       13 |       84 |      127 |
| common.util.concurrent  | ForwardingFluentFuture.java                | src      |       12 |       19 |       13 |       46 |       90 |
| common.util.concurrent  | ForwardingFuture.java                      | src      |       12 |       27 |       14 |       54 |      107 |
| common.util.concurrent  | ForwardingListenableFuture.java            | src      |        9 |       27 |       14 |       27 |       77 |
| common.util.concurrent  | ForwardingListeningExecutorService.java    | src      |        7 |       14 |       13 |       26 |       60 |
| common.util.concurrent  | ForwardingLock.java                        | src      |        9 |        1 |       13 |       35 |       58 |
| common.util.concurrent  | FutureCallback.java                        | src      |        4 |       16 |       13 |       10 |       43 |
| common.util.concurrent  | Futures.java                               | src      |       72 |      713 |      137 |      478 |     1400 |
| common.util.concurrent  | FuturesGetChecked.java                     | src      |       31 |       13 |       43 |      221 |      308 |
| common.util.concurrent  | GwtFluentFutureCatchingSpecialization.java | src      |        3 |        5 |       18 |        9 |       35 |
| common.util.concurrent  | GwtFuturesCatchingSpecialization.java      | src      |        3 |        6 |       18 |        7 |       34 |
| common.util.concurrent  | IgnoreJRERequirement.java                  | src      |        4 |        5 |       13 |        8 |       30 |
| common.util.concurrent  | ImmediateFuture.java                       | src      |       17 |        1 |       17 |       74 |      109 |
| common.util.concurrent  | Internal.java                              | src      |        5 |        8 |       15 |       16 |       44 |
| common.util.concurrent  | InterruptibleTask.java                     | src      |       22 |       25 |       78 |      137 |      262 |
| common.util.concurrent  | JdkFutureAdapters.java                     | src      |       16 |       53 |       30 |       69 |      168 |
| common.util.concurrent  | LazyLogger.java                            | src      |        6 |        1 |       24 |       27 |       58 |
| common.util.concurrent  | ListenableFuture.java                      | src      |        3 |      123 |       28 |       12 |      166 |
| common.util.concurrent  | ListenableFutureTask.java                  | src      |       12 |       31 |       15 |       50 |      108 |
| common.util.concurrent  | ListenableScheduledFuture.java             | src      |        3 |        6 |       13 |        7 |       29 |
| common.util.concurrent  | ListenerCallQueue.java                     | src      |       21 |       62 |       22 |      130 |      235 |
| common.util.concurrent  | ListeningExecutorService.java              | src      |       11 |       72 |       13 |       52 |      148 |
| common.util.concurrent  | ListeningScheduledExecutorService.java     | src      |       12 |       41 |       13 |       46 |      112 |
| common.util.concurrent  | Monitor.java                               | src      |       79 |      466 |      123 |      572 |     1240 |
| common.util.concurrent  | MoreExecutors.java                         | src      |       76 |      387 |       82 |      525 |     1070 |
| common.util.concurrent  | NullnessCasts.java                         | src      |        5 |       40 |       14 |       17 |       76 |
| common.util.concurrent  | OverflowAvoidingLockSupport.java           | src      |        6 |        4 |       16 |       13 |       39 |
| common.util.concurrent  | ParametricNullness.java                    | src      |        4 |       41 |       15 |       12 |       72 |
| common.util.concurrent  | Partially.java                             | src      |        4 |       15 |       13 |       17 |       49 |
| common.util.concurrent  | Platform.java                              | src      |       10 |        1 |       13 |       39 |       63 |
| common.util.concurrent  | RateLimiter.java                           | src      |       37 |      256 |       36 |      168 |      497 |
| common.util.concurrent  | Runnables.java                             | src      |        5 |       10 |       19 |       11 |       45 |
| common.util.concurrent  | SequentialExecutor.java                    | src      |       22 |       45 |       49 |      155 |      271 |
| common.util.concurrent  | Service.java                               | src      |       27 |      202 |       13 |       52 |      294 |
| common.util.concurrent  | ServiceManager.java                        | src      |       71 |      279 |       44 |      498 |      892 |
| common.util.concurrent  | ServiceManagerBridge.java                  | src      |        3 |        5 |       15 |       10 |       33 |
| common.util.concurrent  | SettableFuture.java                        | src      |        7 |       17 |       13 |       27 |       64 |
| common.util.concurrent  | SimpleTimeLimiter.java                     | src      |       32 |       19 |       17 |      215 |      283 |
| common.util.concurrent  | SmoothRateLimiter.java                     | src      |       31 |       95 |      133 |      135 |      394 |
| common.util.concurrent  | SneakyThrows.java                          | src      |        5 |       21 |       15 |       15 |       56 |
| common.util.concurrent  | Striped.java                               | src      |       59 |      165 |       55 |      275 |      554 |
| common.util.concurrent  | ThreadFactoryBuilder.java                  | src      |       13 |       95 |       19 |       99 |      226 |
| common.util.concurrent  | TimeLimiter.java                           | src      |       14 |      219 |       13 |       61 |      307 |
| common.util.concurrent  | TimeoutFuture.java                         | src      |       17 |        8 |       57 |      114 |      196 |
| common.util.concurrent  | TrustedListenableFutureTask.java           | src      |       29 |       15 |       24 |      117 |      185 |
| common.util.concurrent  | UncaughtExceptionHandlers.java             | src      |       10 |       22 |       15 |       43 |       90 |
| common.util.concurrent  | UncheckedExecutionException.java           | src      |        8 |       43 |       22 |       22 |       95 |
| common.util.concurrent  | UncheckedTimeoutException.java             | src      |        7 |       12 |       13 |       19 |       51 |
| common.util.concurrent  | Uninterruptibles.java                      | src      |       36 |      181 |       42 |      362 |      621 |
| common.util.concurrent  | WrappingExecutorService.java               | src      |       22 |       24 |       15 |      107 |      168 |
| common.util.concurrent  | WrappingScheduledExecutorService.java      | src      |        8 |        9 |       13 |       37 |       67 |
| common.util.concurrent  | package-info.java                          | src      |        2 |       12 |       13 |        5 |       32 |
+-------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| common.xml              | ParametricNullness.java                    | src      |        4 |       41 |       15 |       12 |       72 |
| common.xml              | XmlEscapers.java                           | src      |       10 |       61 |       30 |       39 |      140 |
| common.xml              | package-info.java                          | src      |        2 |        8 |       13 |        5 |       28 |
+-------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| thirdparty.publicsuffix | PublicSuffixPatterns.java                  | src      |        6 |       14 |       16 |      633 |      669 |
| thirdparty.publicsuffix | PublicSuffixTrie.java                      | src      |       31 |       76 |       22 |      141 |      270 |
| thirdparty.publicsuffix | PublicSuffixType.java                      | src      |       10 |       13 |       13 |       29 |       65 |
+-------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
| 19 package(s)           | 611 file(s)                                | java     |    18564 |    51891 |    15495 |    97303 |   183253 |
+-------------------------+--------------------------------------------+----------+----------+----------+----------+----------+----------+
```

The **module-info.java** of the **guava** module resides in the default package and is listed with an empty package name, while the **com.google.*** packages are trimmed to their common prefix **com.google**.

The **guava-gwt** module on the other hand gets its package names trimmed. The same run with **-DtrimPkgNames=false** writes them in full:

```
$ cat guava-gwt/sloc.txt
+------------------+--------------------------------------+----------+----------+----------+----------+----------+----------+
| Package Name     | File Name                            | Type     | Blank    | JavaDoc  | Comment  | Code     | Total    |
+------------------+--------------------------------------+----------+----------+----------+----------+----------+----------+
| common           | DummyJavadocClass.java               | src      |        2 |        6 |       15 |        2 |       25 |
| common           | ForceGuavaCompilationEntryPoint.java | src      |        3 |        5 |       15 |        6 |       29 |
+------------------+--------------------------------------+----------+----------+----------+----------+----------+----------+
| 1 package(s)     | 2 file(s)                            | java     |        5 |       11 |       30 |        8 |       54 |
+------------------+--------------------------------------+----------+----------+----------+----------+----------+----------+

$ mvn io.github.orhankupusoglu:sloc-maven-plugin:sloc -Ddisplay=false -Dsave=true -DtrimPkgNames=false

$ cat guava-gwt/sloc.txt
+-------------------+--------------------------------------+----------+----------+----------+----------+----------+----------+
| Package Name      | File Name                            | Type     | Blank    | JavaDoc  | Comment  | Code     | Total    |
+-------------------+--------------------------------------+----------+----------+----------+----------+----------+----------+
| com.google.common | DummyJavadocClass.java               | src      |        2 |        6 |       15 |        2 |       25 |
| com.google.common | ForceGuavaCompilationEntryPoint.java | src      |        3 |        5 |       15 |        6 |       29 |
+-------------------+--------------------------------------+----------+----------+----------+----------+----------+----------+
| 1 package(s)      | 2 file(s)                            | java     |        5 |       11 |       30 |        8 |       54 |
+-------------------+--------------------------------------+----------+----------+----------+----------+----------+----------+

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
