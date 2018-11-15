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

By default the package names are trimmed down to later unique parts to prevent too much repetition.

It can be tested right-away on its own project:

```
$ mvn kupusoglu.orhan:sloc-maven-plugin:sloc
[INFO] Scanning for projects...
[INFO] Inspecting build with total of 1 modules...
[INFO] Installing Nexus Staging features:
[INFO]   ... total of 1 executions of maven-deploy-plugin replaced with nexus-staging-maven-plugin
[INFO] 
[INFO] -----------------< kupusoglu.orhan:sloc-maven-plugin >------------------
[INFO] Building Plugin for SLOC 0.1.3
[INFO] ----------------------------[ maven-plugin ]----------------------------
[INFO] 
[INFO] --- sloc-maven-plugin:0.1.3:sloc (default-cli) @ sloc-maven-plugin ---
[INFO] SLOC - directory: /home/orhanku/ME/DEV/OK/sloc-maven-plugin/src
+------------------+------------------+----------+----------+----------+----------+----------+----------+
| Package Name     | File Name        | Type     | Blank    | JavaDoc  | Comment  | Code     | Total    |
+------------------+------------------+----------+----------+----------+----------+----------+----------+
| engine           | Common.java      | src      |       11 |        3 |        1 |       46 |       61 |
| engine           | CommonTest.java  | test     |       15 |        0 |        2 |       58 |       75 |
| engine           | CountLines.java  | src      |        9 |        0 |        0 |       38 |       47 |
| engine           | CountSLOC.java   | src      |       59 |       12 |        4 |      231 |      306 |
+------------------+------------------+----------+----------+----------+----------+----------+----------+
| goal             | GoalSLOC.java    | src      |        8 |       25 |        0 |       35 |       68 |
+------------------+------------------+----------+----------+----------+----------+----------+----------+
| 2 package(s)     | 5 file(s)        | java     |      102 |       40 |        7 |      408 |      557 |
+------------------+------------------+----------+----------+----------+----------+----------+----------+

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 0.839 s
[INFO] Finished at: 2018-11-15T10:30:44+03:00
[INFO] ------------------------------------------------------------------------
```

&nbsp;

## Parameters

Parameters for the **sloc** goal can be supplied with **-Dname=value**, for example:

```
$ mvn kupusoglu.orhan:sloc-maven-plugin:sloc -DfileExt=cpp

$ mvn kupusoglu.orhan:sloc-maven-plugin:sloc -DtrimPkgNames=false
```

Detailed plugin documentation can be generated with [Maven Site Plugin](https://maven.apache.org/plugins/maven-site-plugin/)'s **mvn site** goal, please check the HTML pages at **target/site/index.html**.
For example: **Project Reports > Plugin Documentation > sloc:sloc**

### goal: sloc
| Parameter      | Default Value | Description                                                 |
| :------------- | ------------- | ----------------------------------------------------------- |
| srcMain        | src           | start in this directory and check files recursively         |
| fileExt        | java          | count SLOC of files with this extension                     |
| trimPkgNames   | true          | trim common prefixes of the package names to remove clutter |

&nbsp;

## Sample Project

[Google Guava](https://en.wikipedia.org/wiki/Google_Guava) is a well-known Java project.

```
$ git clone https://github.com/google/guava.git
$ cd guava/

$ git status
On branch master
Your branch is up-to-date with 'origin/master'.
nothing to commit, working directory clean

$ git log --oneline -n 5
0ac4a8b common.graph PackageSanityTests: specify a default object for EndpointPair inputs. This should resolve a few internal flaky test issues
5f47d39 Standardise message format for "duplicate key" IllegalArgumentException thrown from ImmutableTable.Builder.build().
2d86514 Correct documentation for Splitter#withKeyValueSeparator(Splitter)
c367e94 Use maven-compiler-plugin version 3.8.0.
f3542ee Add MediaType.MICROSOFT_OUTLOOK and add missing javadocs to a few other MediaTypes.

$ mvn kupusoglu.orhan:sloc-maven-plugin:sloc
```
The second run will give a simpler output:

```
$ mvn kupusoglu.orhan:sloc-maven-plugin:sloc
[INFO] Scanning for projects...
[WARNING] The project com.google.guava:guava-parent:pom:HEAD-jre-SNAPSHOT uses prerequisites which is only intended for maven-plugin projects but not for non maven-plugin projects. For such purposes you should use the maven-enforcer-plugin. See https://maven.apache.org/enforcer/enforcer-rules/requireMavenVersion.html
[INFO] ------------------------------------------------------------------------
[INFO] Reactor Build Order:
[INFO] 
[INFO] Guava Maven Parent                                                 [pom]
[INFO] Guava: Google Core Libraries for Java                           [bundle]
[INFO] Guava Testing Library                                              [jar]
[INFO] Guava Unit Tests                                                   [jar]
[INFO] Guava GWT compatible libs                                          [jar]
[INFO] 
[INFO] -------------------< com.google.guava:guava-parent >--------------------
[INFO] Building Guava Maven Parent HEAD-jre-SNAPSHOT                      [1/5]
[INFO] --------------------------------[ pom ]---------------------------------
[INFO] 
[INFO] --- sloc-maven-plugin:0.1.3:sloc (default-cli) @ guava-parent ---
[WARNING] Does not contain a source directory: /home/orhanku/ME/DEV/x/guava/src
[INFO] 
[INFO] -----------------------< com.google.guava:guava >-----------------------
[INFO] Building Guava: Google Core Libraries for Java HEAD-jre-SNAPSHOT   [2/5]
[INFO] -------------------------------[ bundle ]-------------------------------
[INFO] 
[INFO] --- sloc-maven-plugin:0.1.3:sloc (default-cli) @ guava ---
[INFO] SLOC - directory: /home/orhanku/ME/DEV/x/guava/guava/src
+-------------------------+-----------------------------------------------------+----------+----------+----------+----------+----------+----------+
| Package Name            | File Name                                           | Type     | Blank    | JavaDoc  | Comment  | Code     | Total    |
+-------------------------+-----------------------------------------------------+----------+----------+----------+----------+----------+----------+
| common.annotations      | Beta.java                                           | src      |        3 |       14 |       13 |       17 |       47 |
| common.annotations      | GwtCompatible.java                                  | src      |        5 |       54 |       13 |       14 |       86 |
| common.annotations      | GwtIncompatible.java                                | src      |        3 |       18 |       13 |       13 |       47 |
| common.annotations      | VisibleForTesting.java                              | src      |        2 |        6 |       13 |        4 |       25 |
| common.annotations      | package-info.java                                   | src      |        1 |        4 |       13 |        1 |       19 |
+-------------------------+-----------------------------------------------------+----------+----------+----------+----------+----------+----------+
| common.base.internal    | Finalizer.java                                      | src      |       23 |       33 |       44 |      135 |      235 |
+-------------------------+-----------------------------------------------------+----------+----------+----------+----------+----------+----------+
| common.base             | Absent.java                                         | src      |       19 |        0 |       13 |       67 |       99 |
| common.base             | AbstractIterator.java                               | src      |       13 |        4 |       13 |       60 |       90 |
| common.base             | Ascii.java                                          | src      |       61 |      391 |       23 |      160 |      635 |
| common.base             | CaseFormat.java                                     | src      |       28 |       17 |       15 |      149 |      209 |
| common.base             | CharMatcher.java                                    | src      |      250 |      410 |       87 |     1066 |     1813 |
| common.base             | Charsets.java                                       | src      |       10 |       55 |       19 |       18 |      102 |
| common.base             | CommonMatcher.java                                  | src      |        8 |        5 |       13 |       11 |       37 |
| common.base             | CommonPattern.java                                  | src      |        8 |        5 |       14 |       16 |       43 |
| common.base             | Converter.java                                      | src      |       65 |      167 |       35 |      256 |      523 |
| common.base             | Defaults.java                                       | src      |        6 |       11 |       13 |       33 |       63 |
| common.base             | Enums.java                                          | src      |       20 |       29 |       13 |       89 |      151 |
| common.base             | Equivalence.java                                    | src      |       43 |      162 |       19 |      154 |      378 |
| common.base             | ExtraObjectsMethodsForWeb.java                      | src      |        3 |        4 |       13 |        4 |       24 |
| common.base             | FinalizablePhantomReference.java                    | src      |        3 |       16 |       13 |       12 |       44 |
| common.base             | FinalizableReference.java                           | src      |        3 |       12 |       13 |        6 |       34 |
| common.base             | FinalizableReferenceQueue.java                      | src      |       28 |       87 |       74 |      163 |      352 |
| common.base             | FinalizableSoftReference.java                       | src      |        3 |       14 |       13 |       12 |       42 |
| common.base             | FinalizableWeakReference.java                       | src      |        3 |       14 |       13 |       12 |       42 |
| common.base             | Function.java                                       | src      |        4 |       32 |       13 |       14 |       63 |
| common.base             | FunctionalEquivalence.java                          | src      |       12 |        6 |       13 |       44 |       75 |
| common.base             | Functions.java                                      | src      |       61 |       91 |       18 |      231 |      401 |
| common.base             | JdkPattern.java                                     | src      |       17 |        0 |       13 |       60 |       90 |
| common.base             | Joiner.java                                         | src      |       43 |      180 |       13 |      242 |      478 |
| common.base             | MoreObjects.java                                    | src      |       31 |      196 |       14 |      146 |      387 |
| common.base             | Objects.java                                        | src      |        5 |       47 |       13 |       14 |       79 |
| common.base             | Optional.java                                       | src      |       24 |      244 |       13 |       72 |      353 |
| common.base             | PairwiseEquivalence.java                            | src      |       14 |        0 |       13 |       48 |       75 |
| common.base             | PatternCompiler.java                                | src      |        4 |       14 |       13 |        7 |       38 |
| common.base             | Platform.java                                       | src      |       17 |        5 |       13 |       62 |       97 |
| common.base             | Preconditions.java                                  | src      |       88 |      723 |       42 |      565 |     1418 |
| common.base             | Predicate.java                                      | src      |        5 |       47 |       13 |       16 |       81 |
| common.base             | Predicates.java                                     | src      |      103 |      124 |       27 |      449 |      703 |
| common.base             | Present.java                                        | src      |       17 |        0 |       13 |       69 |       99 |
| common.base             | SmallCharMatcher.java                               | src      |       13 |       11 |       32 |       90 |      146 |
| common.base             | Splitter.java                                       | src      |       54 |      254 |       30 |      275 |      613 |
| common.base             | StandardSystemProperty.java                         | src      |       36 |       10 |       13 |       78 |      137 |
| common.base             | Stopwatch.java                                      | src      |       22 |      109 |       14 |      126 |      271 |
| common.base             | Strings.java                                        | src      |       24 |      125 |       19 |      145 |      313 |
| common.base             | Supplier.java                                       | src      |        3 |       24 |       13 |       10 |       50 |
| common.base             | Suppliers.java                                      | src      |       47 |       64 |       32 |      222 |      365 |
| common.base             | Throwables.java                                     | src      |       34 |      246 |       37 |      224 |      541 |
| common.base             | Ticker.java                                         | src      |        6 |       16 |       13 |       23 |       58 |
| common.base             | Utf8.java                                           | src      |       15 |       42 |       35 |      109 |      201 |
| common.base             | Verify.java                                         | src      |       31 |      287 |       15 |      175 |      508 |
| common.base             | VerifyException.java                                | src      |        6 |       19 |       13 |       18 |       56 |
| common.base             | package-info.java                                   | src      |        2 |       43 |       13 |        5 |       63 |
+-------------------------+-----------------------------------------------------+----------+----------+----------+----------+----------+----------+
| common.cache            | AbstractCache.java                                  | src      |       30 |       76 |       13 |      137 |      256 |
| common.cache            | AbstractLoadingCache.java                           | src      |        8 |       15 |       13 |       40 |       76 |
| common.cache            | Cache.java                                          | src      |       15 |      114 |       13 |       30 |      172 |
| common.cache            | CacheBuilder.java                                   | src      |       73 |      537 |       17 |      388 |     1015 |
| common.cache            | CacheBuilderSpec.java                               | src      |       45 |       65 |       15 |      354 |      479 |
| common.cache            | CacheLoader.java                                    | src      |       22 |      115 |       17 |       97 |      251 |
| common.cache            | CacheStats.java                                     | src      |       23 |      116 |       13 |      124 |      276 |
| common.cache            | ForwardingCache.java                                | src      |       20 |       14 |       13 |       81 |      128 |
| common.cache            | ForwardingLoadingCache.java                         | src      |       13 |       17 |       13 |       44 |       87 |
| common.cache            | LoadingCache.java                                   | src      |        9 |      118 |       13 |       20 |      160 |
| common.cache            | LocalCache.java                                     | src      |      723 |      293 |      281 |     3697 |     4994 |
| common.cache            | LongAddable.java                                    | src      |        5 |        5 |       13 |        8 |       31 |
| common.cache            | LongAddables.java                                   | src      |        8 |        5 |       13 |       47 |       73 |
| common.cache            | LongAdder.java                                      | src      |       19 |       57 |        9 |       99 |      184 |
| common.cache            | ReferenceEntry.java                                 | src      |       21 |       20 |       23 |       43 |      107 |
| common.cache            | RemovalCause.java                                   | src      |        8 |       34 |       13 |       39 |       94 |
| common.cache            | RemovalListener.java                                | src      |        3 |       22 |       15 |        7 |       47 |
| common.cache            | RemovalListeners.java                               | src      |        6 |       13 |       13 |       25 |       57 |
| common.cache            | RemovalNotification.java                            | src      |        9 |       22 |       13 |       25 |       69 |
| common.cache            | Striped64.java                                      | src      |       24 |       44 |       73 |      169 |      310 |
| common.cache            | Weigher.java                                        | src      |        4 |       12 |       13 |        7 |       36 |
| common.cache            | package-info.java                                   | src      |        2 |       17 |       13 |        3 |       35 |
+-------------------------+-----------------------------------------------------+----------+----------+----------+----------+----------+----------+
| common.collect          | AbstractBiMap.java                                  | src      |       77 |       13 |       42 |      353 |      485 |
| common.collect          | AbstractIndexedListIterator.java                    | src      |       13 |       24 |       15 |       50 |      102 |
| common.collect          | AbstractIterator.java                               | src      |       16 |       73 |       17 |       68 |      174 |
| common.collect          | AbstractListMultimap.java                           | src      |       15 |       53 |       16 |       57 |      141 |
| common.collect          | AbstractMapBasedMultimap.java                       | src      |      236 |      152 |       53 |     1198 |     1639 |
| common.collect          | AbstractMapBasedMultiset.java                       | src      |       44 |       21 |       31 |      242 |      338 |
| common.collect          | AbstractMapEntry.java                               | src      |        9 |        6 |       15 |       36 |       66 |
| common.collect          | AbstractMultimap.java                               | src      |       45 |       19 |       18 |      183 |      265 |
| common.collect          | AbstractMultiset.java                               | src      |       36 |       40 |       20 |      135 |      231 |
| common.collect          | AbstractNavigableMap.java                           | src      |       28 |        5 |       15 |      121 |      169 |
| common.collect          | AbstractRangeSet.java                               | src      |       17 |        5 |       13 |       65 |      100 |
| common.collect          | AbstractSequentialIterator.java                     | src      |        7 |       28 |       15 |       28 |       78 |
| common.collect          | AbstractSetMultimap.java                            | src      |       16 |       58 |       16 |       62 |      152 |
| common.collect          | AbstractSortedKeySortedSetMultimap.java             | src      |        8 |        8 |       15 |       28 |       59 |
| common.collect          | AbstractSortedMultiset.java                         | src      |       21 |        9 |       15 |      102 |      147 |
| common.collect          | AbstractSortedSetMultimap.java                      | src      |       14 |       57 |       16 |       61 |      148 |
| common.collect          | AbstractTable.java                                  | src      |       39 |        5 |       13 |      185 |      242 |
| common.collect          | AllEqualOrdering.java                               | src      |       10 |        5 |       15 |       34 |       64 |
| common.collect          | ArrayListMultimap.java                              | src      |       16 |       68 |       16 |       71 |      171 |
| common.collect          | ArrayListMultimapGwtSerializationDependencies.java  | src      |        3 |        9 |       16 |       11 |       39 |
| common.collect          | ArrayTable.java                                     | src      |       94 |      217 |       37 |      429 |      777 |
| common.collect          | BaseImmutableMultimap.java                          | src      |        2 |        4 |       15 |        4 |       25 |
| common.collect          | BiMap.java                                          | src      |       10 |       59 |       18 |       21 |      108 |
| common.collect          | BoundType.java                                      | src      |        7 |        7 |       13 |       19 |       46 |
| common.collect          | ByFunctionOrdering.java                             | src      |       10 |        4 |       15 |       40 |       69 |
| common.collect          | CartesianList.java                                  | src      |       15 |        5 |       13 |       91 |      124 |
| common.collect          | ClassToInstanceMap.java                             | src      |        4 |       31 |       15 |       12 |       62 |
| common.collect          | CollectCollectors.java                              | src      |       14 |        0 |       19 |      101 |      134 |
| common.collect          | CollectPreconditions.java                           | src      |        9 |        4 |       15 |       37 |       65 |
| common.collect          | CollectSpliterators.java                            | src      |       33 |        8 |       15 |      234 |      290 |
| common.collect          | Collections2.java                                   | src      |       75 |      170 |       24 |      425 |      694 |
| common.collect          | CompactHashMap.java                                 | src      |      109 |       93 |       40 |      603 |      845 |
| common.collect          | CompactHashSet.java                                 | src      |       63 |       96 |       31 |      376 |      566 |
| common.collect          | CompactLinkedHashMap.java                           | src      |       36 |       47 |       18 |      186 |      287 |
| common.collect          | CompactLinkedHashSet.java                           | src      |       32 |       56 |       18 |      128 |      234 |
| common.collect          | ComparatorOrdering.java                             | src      |       10 |        0 |       15 |       38 |       63 |
| common.collect          | Comparators.java                                    | src      |       11 |       90 |       18 |       72 |      191 |
| common.collect          | ComparisonChain.java                                | src      |       37 |       80 |       15 |      118 |      250 |
| common.collect          | CompoundOrdering.java                               | src      |       10 |        0 |       15 |       46 |       71 |
| common.collect          | ComputationException.java                           | src      |        4 |        6 |       15 |       11 |       36 |
| common.collect          | ConcurrentHashMultiset.java                         | src      |       57 |       97 |       73 |      375 |      602 |
| common.collect          | ConsumingQueueIterator.java                         | src      |        7 |        4 |       13 |       21 |       45 |
| common.collect          | ContiguousSet.java                                  | src      |       27 |       93 |       18 |      120 |      258 |
| common.collect          | Count.java                                          | src      |       12 |        5 |       13 |       40 |       70 |
| common.collect          | Cut.java                                            | src      |       86 |        9 |       29 |      346 |      470 |
| common.collect          | DenseImmutableTable.java                            | src      |       45 |        0 |       18 |      214 |      277 |
| common.collect          | DescendingImmutableSortedMultiset.java              | src      |       14 |        5 |       13 |       51 |       83 |
| common.collect          | DescendingImmutableSortedSet.java                   | src      |       19 |        5 |       15 |       80 |      119 |
| common.collect          | DescendingMultiset.java                             | src      |       26 |        6 |       15 |      110 |      157 |
| common.collect          | DiscreteDomain.java                                 | src      |       48 |       84 |       15 |      175 |      322 |
| common.collect          | EmptyContiguousSet.java                             | src      |       28 |        5 |       13 |      122 |      168 |
| common.collect          | EmptyImmutableListMultimap.java                     | src      |        6 |        5 |       15 |       13 |       39 |
| common.collect          | EmptyImmutableSetMultimap.java                      | src      |        6 |        5 |       15 |       13 |       39 |
| common.collect          | EnumBiMap.java                                      | src      |       16 |       29 |       15 |       84 |      144 |
| common.collect          | EnumHashBiMap.java                                  | src      |       15 |       30 |       16 |       66 |      127 |
| common.collect          | EnumMultiset.java                                   | src      |       30 |       33 |       16 |      239 |      318 |
| common.collect          | EvictingQueue.java                                  | src      |       17 |       39 |       16 |       70 |      142 |
| common.collect          | ExplicitOrdering.java                               | src      |       11 |        0 |       15 |       44 |       70 |
| common.collect          | FilteredEntryMultimap.java                          | src      |       52 |        6 |       16 |      342 |      416 |
| common.collect          | FilteredEntrySetMultimap.java                       | src      |       10 |        5 |       15 |       36 |       66 |
| common.collect          | FilteredKeyListMultimap.java                        | src      |        7 |        5 |       15 |       28 |       55 |
| common.collect          | FilteredKeyMultimap.java                            | src      |       33 |        5 |       14 |      168 |      220 |
| common.collect          | FilteredKeySetMultimap.java                         | src      |       12 |        5 |       15 |       47 |       79 |
| common.collect          | FilteredMultimap.java                               | src      |        4 |        5 |       15 |        9 |       33 |
| common.collect          | FilteredMultimapValues.java                         | src      |       12 |        5 |       15 |       63 |       95 |
| common.collect          | FilteredSetMultimap.java                            | src      |        3 |        5 |       15 |        7 |       30 |
| common.collect          | FluentIterable.java                                 | src      |       53 |      523 |       25 |      250 |      851 |
| common.collect          | ForwardingBlockingDeque.java                        | src      |       20 |       23 |       15 |       74 |      132 |
| common.collect          | ForwardingCollection.java                           | src      |       29 |       98 |       16 |      113 |      256 |
| common.collect          | ForwardingConcurrentMap.java                        | src      |        9 |       15 |       15 |       32 |       71 |
| common.collect          | ForwardingDeque.java                                | src      |       22 |       17 |       15 |       89 |      143 |
| common.collect          | ForwardingImmutableCollection.java                  | src      |        3 |        5 |       15 |        6 |       29 |
| common.collect          | ForwardingImmutableList.java                        | src      |        3 |        5 |       15 |        6 |       29 |
| common.collect          | ForwardingImmutableMap.java                         | src      |        3 |        5 |       15 |        6 |       29 |
| common.collect          | ForwardingImmutableSet.java                         | src      |        3 |        5 |       15 |        6 |       29 |
| common.collect          | ForwardingIterator.java                             | src      |        8 |       15 |       15 |       24 |       62 |
| common.collect          | ForwardingList.java                                 | src      |       27 |       95 |       16 |      102 |      240 |
| common.collect          | ForwardingListIterator.java                         | src      |       11 |       15 |       15 |       37 |       78 |
| common.collect          | ForwardingListMultimap.java                         | src      |        8 |       12 |       15 |       27 |       62 |
| common.collect          | ForwardingMap.java                                  | src      |       32 |      121 |       16 |      136 |      305 |
| common.collect          | ForwardingMapEntry.java                             | src      |       13 |       44 |       16 |       51 |      124 |
| common.collect          | ForwardingMultimap.java                             | src      |       25 |       12 |       15 |      101 |      153 |
| common.collect          | ForwardingMultiset.java                             | src      |       32 |      138 |       15 |      129 |      314 |
| common.collect          | ForwardingNavigableMap.java                         | src      |       50 |      136 |       15 |      206 |      407 |
| common.collect          | ForwardingNavigableSet.java                         | src      |       29 |       86 |       15 |      105 |      235 |
| common.collect          | ForwardingObject.java                               | src      |        7 |       30 |       15 |       15 |       67 |
| common.collect          | ForwardingQueue.java                                | src      |       13 |       40 |       15 |       56 |      124 |
| common.collect          | ForwardingSet.java                                  | src      |       11 |       42 |       16 |       31 |      100 |
| common.collect          | ForwardingSetMultimap.java                          | src      |        8 |       12 |       15 |       30 |       65 |
| common.collect          | ForwardingSortedMap.java                            | src      |       16 |       46 |       18 |       73 |      153 |
| common.collect          | ForwardingSortedMultiset.java                       | src      |       22 |       81 |       13 |      110 |      226 |
| common.collect          | ForwardingSortedSet.java                            | src      |       15 |       48 |       18 |       81 |      162 |
| common.collect          | ForwardingSortedSetMultimap.java                    | src      |        9 |       12 |       15 |       29 |       65 |
| common.collect          | ForwardingTable.java                                | src      |       25 |        8 |       15 |       99 |      147 |
| common.collect          | GeneralRange.java                                   | src      |       33 |       24 |       15 |      223 |      295 |
| common.collect          | GwtTransient.java                                   | src      |        4 |        5 |       15 |       12 |       36 |
| common.collect          | HashBasedTable.java                                 | src      |       20 |       37 |       16 |       72 |      145 |
| common.collect          | HashBiMap.java                                      | src      |       99 |       30 |       19 |      605 |      753 |
| common.collect          | HashMultimap.java                                   | src      |       14 |       55 |       15 |       57 |      141 |
| common.collect          | HashMultimapGwtSerializationDependencies.java       | src      |        3 |        9 |       15 |       10 |       37 |
| common.collect          | HashMultiset.java                                   | src      |       11 |       25 |       15 |       42 |       93 |
| common.collect          | Hashing.java                                        | src      |        9 |        7 |       30 |       29 |       75 |
| common.collect          | ImmutableAsList.java                                | src      |       13 |        7 |       17 |       48 |       85 |
| common.collect          | ImmutableBiMap.java                                 | src      |       36 |      175 |       30 |      194 |      435 |
| common.collect          | ImmutableBiMapFauxverideShim.java                   | src      |        4 |       23 |       15 |       21 |       63 |
| common.collect          | ImmutableClassToInstanceMap.java                    | src      |       20 |       72 |       16 |       84 |      192 |
| common.collect          | ImmutableCollection.java                            | src      |       34 |      245 |       25 |      165 |      469 |
| common.collect          | ImmutableEntry.java                                 | src      |        8 |        0 |       15 |       27 |       50 |
| common.collect          | ImmutableEnumMap.java                               | src      |       21 |        5 |       19 |       89 |      134 |
| common.collect          | ImmutableEnumSet.java                               | src      |       23 |        5 |       28 |      100 |      156 |
| common.collect          | ImmutableList.java                                  | src      |       89 |      290 |       28 |      462 |      869 |
| common.collect          | ImmutableListMultimap.java                          | src      |       46 |      164 |       20 |      281 |      511 |
| common.collect          | ImmutableMap.java                                   | src      |       96 |      284 |       45 |      502 |      927 |
| common.collect          | ImmutableMapEntry.java                              | src      |       16 |       19 |       16 |       61 |      112 |
| common.collect          | ImmutableMapEntrySet.java                           | src      |       23 |        6 |       15 |       92 |      136 |
| common.collect          | ImmutableMapKeySet.java                             | src      |       17 |        6 |       15 |       61 |       99 |
| common.collect          | ImmutableMapValues.java                             | src      |       20 |        6 |       15 |       83 |      124 |
| common.collect          | ImmutableMultimap.java                              | src      |       90 |      199 |       22 |      441 |      752 |
| common.collect          | ImmutableMultiset.java                              | src      |       73 |      196 |       16 |      359 |      644 |
| common.collect          | ImmutableMultisetGwtSerializationDependencies.java  | src      |        3 |       18 |       15 |        4 |       40 |
| common.collect          | ImmutableRangeMap.java                              | src      |       42 |       63 |       13 |      293 |      411 |
| common.collect          | ImmutableRangeSet.java                              | src      |       89 |      152 |       19 |      571 |      831 |
| common.collect          | ImmutableSet.java                                   | src      |       87 |      203 |       39 |      505 |      834 |
| common.collect          | ImmutableSetMultimap.java                           | src      |       62 |      200 |       21 |      369 |      652 |
| common.collect          | ImmutableSortedAsList.java                          | src      |       13 |        6 |       25 |       51 |       95 |
| common.collect          | ImmutableSortedMap.java                             | src      |       87 |      292 |       42 |      528 |      949 |
| common.collect          | ImmutableSortedMapFauxverideShim.java               | src      |       12 |       84 |       16 |       50 |      162 |
| common.collect          | ImmutableSortedMultiset.java                        | src      |       46 |      251 |       18 |      278 |      593 |
| common.collect          | ImmutableSortedMultisetFauxverideShim.java          | src      |       13 |      108 |       28 |       50 |      199 |
| common.collect          | ImmutableSortedSet.java                             | src      |       74 |      311 |       45 |      413 |      843 |
| common.collect          | ImmutableSortedSetFauxverideShim.java               | src      |       13 |      104 |       33 |       46 |      196 |
| common.collect          | ImmutableTable.java                                 | src      |       62 |      151 |       20 |      332 |      565 |
| common.collect          | IndexedImmutableSet.java                            | src      |       12 |        0 |       15 |       52 |       79 |
| common.collect          | Interner.java                                       | src      |        3 |       19 |       15 |       10 |       47 |
| common.collect          | Interners.java                                      | src      |       25 |       42 |       22 |       99 |      188 |
| common.collect          | Iterables.java                                      | src      |       77 |      483 |       25 |      445 |     1030 |
| common.collect          | Iterators.java                                      | src      |      121 |      459 |       48 |      753 |     1381 |
| common.collect          | JdkBackedImmutableBiMap.java                        | src      |       14 |        4 |       15 |       88 |      121 |
| common.collect          | JdkBackedImmutableMap.java                          | src      |       13 |        9 |       15 |       56 |       93 |
| common.collect          | JdkBackedImmutableMultiset.java                     | src      |       12 |        6 |       13 |       59 |       90 |
| common.collect          | JdkBackedImmutableSet.java                          | src      |        8 |        7 |       13 |       29 |       57 |
| common.collect          | LexicographicalOrdering.java                        | src      |        9 |        0 |       15 |       52 |       76 |
| common.collect          | LinkedHashMultimap.java                             | src      |       73 |      113 |       23 |      405 |      614 |
| common.collect          | LinkedHashMultimapGwtSerializationDependencies.java | src      |        3 |        9 |       15 |       11 |       38 |
| common.collect          | LinkedHashMultiset.java                             | src      |       11 |       33 |       15 |       43 |      102 |
| common.collect          | LinkedListMultimap.java                             | src      |       92 |      141 |       33 |      588 |      854 |
| common.collect          | ListMultimap.java                                   | src      |        7 |       53 |       15 |       22 |       97 |
| common.collect          | Lists.java                                          | src      |      123 |      358 |       27 |      646 |     1154 |
| common.collect          | MapDifference.java                                  | src      |       13 |       53 |       15 |       26 |      107 |
| common.collect          | MapMaker.java                                       | src      |       25 |      141 |       16 |      128 |      310 |
| common.collect          | MapMakerInternalMap.java                            | src      |      415 |      244 |      142 |     2148 |     2949 |
| common.collect          | Maps.java                                           | src      |      489 |     1216 |       59 |     2506 |     4270 |
| common.collect          | MinMaxPriorityQueue.java                            | src      |       86 |      235 |       65 |      570 |      956 |
| common.collect          | MoreCollectors.java                                 | src      |       18 |       22 |       19 |      111 |      170 |
| common.collect          | Multimap.java                                       | src      |       29 |      287 |       20 |       50 |      386 |
| common.collect          | MultimapBuilder.java                                | src      |       56 |      126 |       24 |      263 |      469 |
| common.collect          | Multimaps.java                                      | src      |      211 |      839 |       25 |     1122 |     2197 |
| common.collect          | Multiset.java                                       | src      |       35 |      331 |       22 |       87 |      475 |
| common.collect          | Multisets.java                                      | src      |      128 |      234 |       36 |      780 |     1178 |
| common.collect          | MutableClassToInstanceMap.java                      | src      |       27 |       23 |       15 |      116 |      181 |
| common.collect          | NaturalOrdering.java                                | src      |       13 |        0 |       16 |       48 |       77 |
| common.collect          | NullsFirstOrdering.java                             | src      |       12 |        0 |       16 |       58 |       86 |
| common.collect          | NullsLastOrdering.java                              | src      |       12 |        0 |       16 |       58 |       86 |
| common.collect          | ObjectArrays.java                                   | src      |       19 |       89 |       17 |      104 |      229 |
| common.collect          | Ordering.java                                       | src      |       66 |      545 |       64 |      283 |      958 |
| common.collect          | PeekingIterator.java                                | src      |        5 |       35 |       15 |       14 |       69 |
| common.collect          | Platform.java                                       | src      |       15 |       32 |       17 |       49 |      113 |
| common.collect          | Queues.java                                         | src      |       32 |      168 |       31 |      193 |      424 |
| common.collect          | Range.java                                          | src      |       59 |      348 |       17 |      285 |      709 |
| common.collect          | RangeGwtSerializationDependencies.java              | src      |        3 |        9 |       15 |        5 |       32 |
| common.collect          | RangeMap.java                                       | src      |       16 |       97 |       15 |       35 |      163 |
| common.collect          | RangeSet.java                                       | src      |       29 |      176 |       18 |       53 |      276 |
| common.collect          | RegularContiguousSet.java                           | src      |       34 |        5 |       15 |      184 |      238 |
| common.collect          | RegularImmutableAsList.java                         | src      |       14 |        6 |       15 |       55 |       90 |
| common.collect          | RegularImmutableBiMap.java                          | src      |       43 |        9 |       16 |      237 |      305 |
| common.collect          | RegularImmutableList.java                           | src      |       15 |        5 |       19 |       53 |       92 |
| common.collect          | RegularImmutableMap.java                            | src      |       45 |       29 |       29 |      210 |      313 |
| common.collect          | RegularImmutableMultiset.java                       | src      |       23 |       19 |       13 |      140 |      195 |
| common.collect          | RegularImmutableSet.java                            | src      |       17 |        5 |       17 |       83 |      122 |
| common.collect          | RegularImmutableSortedMultiset.java                 | src      |       20 |        5 |       13 |       95 |      133 |
| common.collect          | RegularImmutableSortedSet.java                      | src      |       44 |        7 |       27 |      247 |      325 |
| common.collect          | RegularImmutableTable.java                          | src      |       22 |        5 |       26 |      133 |      186 |
| common.collect          | ReverseNaturalOrdering.java                         | src      |       20 |        0 |       17 |       64 |      101 |
| common.collect          | ReverseOrdering.java                                | src      |       20 |        0 |       16 |       75 |      111 |
| common.collect          | RowSortedTable.java                                 | src      |        4 |       22 |       15 |       13 |       54 |
| common.collect          | Serialization.java                                  | src      |       18 |       63 |       17 |      121 |      219 |
| common.collect          | SetMultimap.java                                    | src      |        8 |       70 |       15 |       25 |      118 |
| common.collect          | Sets.java                                           | src      |      207 |      732 |       64 |     1124 |     2127 |
| common.collect          | SingletonImmutableBiMap.java                        | src      |       17 |        6 |       16 |       66 |      105 |
| common.collect          | SingletonImmutableList.java                         | src      |       13 |        5 |       15 |       44 |       77 |
| common.collect          | SingletonImmutableSet.java                          | src      |       15 |        6 |       24 |       58 |      103 |
| common.collect          | SingletonImmutableTable.java                        | src      |       13 |        5 |       15 |       49 |       82 |
| common.collect          | SortedIterable.java                                 | src      |        4 |       14 |       13 |       10 |       41 |
| common.collect          | SortedIterables.java                                | src      |        6 |        9 |       14 |       30 |       59 |
| common.collect          | SortedLists.java                                    | src      |       14 |       92 |       21 |      153 |      280 |
| common.collect          | SortedMapDifference.java                            | src      |        7 |        6 |       15 |       14 |       42 |
| common.collect          | SortedMultiset.java                                 | src      |       14 |       89 |       15 |       26 |      144 |
| common.collect          | SortedMultisetBridge.java                           | src      |        3 |        7 |       15 |        8 |       33 |
| common.collect          | SortedMultisets.java                                | src      |       28 |        5 |       15 |      123 |      171 |
| common.collect          | SortedSetMultimap.java                              | src      |        8 |       61 |       16 |       23 |      108 |
| common.collect          | SparseImmutableTable.java                           | src      |       15 |        0 |       18 |      102 |      135 |
| common.collect          | StandardRowSortedTable.java                         | src      |       18 |       29 |       20 |       76 |      143 |
| common.collect          | StandardTable.java                                  | src      |      124 |       51 |       35 |      780 |      990 |
| common.collect          | Streams.java                                        | src      |       71 |      308 |       29 |      460 |      868 |
| common.collect          | Synchronized.java                                   | src      |      327 |       12 |       24 |     1799 |     2162 |
| common.collect          | Table.java                                          | src      |       32 |      168 |       19 |       63 |      282 |
| common.collect          | Tables.java                                         | src      |       90 |      167 |       25 |      454 |      736 |
| common.collect          | TopKSelector.java                                   | src      |       26 |       88 |       25 |      132 |      271 |
| common.collect          | TransformedIterator.java                            | src      |        9 |        6 |       15 |       24 |       54 |
| common.collect          | TransformedListIterator.java                        | src      |       10 |        6 |       15 |       38 |       69 |
| common.collect          | TreeBasedTable.java                                 | src      |       46 |       74 |       24 |      208 |      352 |
| common.collect          | TreeMultimap.java                                   | src      |       20 |       88 |       15 |       99 |      222 |
| common.collect          | TreeMultiset.java                                   | src      |      106 |       54 |       36 |      832 |     1028 |
| common.collect          | TreeRangeMap.java                                   | src      |       95 |        9 |       31 |      572 |      707 |
| common.collect          | TreeRangeSet.java                                   | src      |       99 |       32 |       58 |      737 |      926 |
| common.collect          | TreeTraverser.java                                  | src      |       28 |       83 |       16 |      164 |      291 |
| common.collect          | UnmodifiableIterator.java                           | src      |        4 |       16 |       15 |       13 |       48 |
| common.collect          | UnmodifiableListIterator.java                       | src      |        5 |       18 |       15 |       19 |       57 |
| common.collect          | UnmodifiableSortedMultiset.java                     | src      |       17 |        7 |       15 |       71 |      110 |
| common.collect          | UsingToStringOrdering.java                          | src      |        8 |        0 |       16 |       21 |       45 |
| common.collect          | WellBehavedMap.java                                 | src      |       11 |       14 |       15 |       59 |       99 |
| common.collect          | package-info.java                                   | src      |        2 |      197 |       15 |        5 |      219 |
+-------------------------+-----------------------------------------------------+----------+----------+----------+----------+----------+----------+
| common.escape           | ArrayBasedCharEscaper.java                          | src      |       11 |       61 |       24 |       53 |      149 |
| common.escape           | ArrayBasedEscaperMap.java                           | src      |        9 |       20 |       20 |       35 |       84 |
| common.escape           | ArrayBasedUnicodeEscaper.java                       | src      |       12 |       64 |       41 |       86 |      203 |
| common.escape           | CharEscaper.java                                    | src      |       19 |       57 |       26 |       73 |      175 |
| common.escape           | CharEscaperBuilder.java                             | src      |       14 |       26 |       19 |       69 |      128 |
| common.escape           | Escaper.java                                        | src      |        6 |       57 |       14 |       20 |       97 |
| common.escape           | Escapers.java                                       | src      |       21 |       97 |       32 |      121 |      271 |
| common.escape           | Platform.java                                       | src      |        5 |       10 |       13 |       17 |       45 |
| common.escape           | UnicodeEscaper.java                                 | src      |       15 |      139 |       26 |      122 |      302 |
| common.escape           | package-info.java                                   | src      |        2 |       12 |       13 |        5 |       32 |
+-------------------------+-----------------------------------------------------+----------+----------+----------+----------+----------+----------+
| common.eventbus         | AllowConcurrentEvents.java                          | src      |        3 |        9 |       13 |       10 |       35 |
| common.eventbus         | AsyncEventBus.java                                  | src      |        6 |       30 |       13 |       15 |       64 |
| common.eventbus         | DeadEvent.java                                      | src      |        9 |       27 |       13 |       23 |       72 |
| common.eventbus         | Dispatcher.java                                     | src      |       25 |       31 |       31 |      103 |      190 |
| common.eventbus         | EventBus.java                                       | src      |       22 |      104 |       15 |      112 |      253 |
| common.eventbus         | Subscribe.java                                      | src      |        3 |       13 |       13 |       10 |       39 |
| common.eventbus         | Subscriber.java                                     | src      |       20 |       21 |       16 |       91 |      148 |
| common.eventbus         | SubscriberExceptionContext.java                     | src      |        9 |       16 |       13 |       31 |       69 |
| common.eventbus         | SubscriberExceptionHandler.java                     | src      |        2 |        5 |       13 |        5 |       25 |
| common.eventbus         | SubscriberRegistry.java                             | src      |       33 |       28 |       22 |      170 |      253 |
| common.eventbus         | package-info.java                                   | src      |        2 |      237 |       13 |        5 |      257 |
+-------------------------+-----------------------------------------------------+----------+----------+----------+----------+----------+----------+
| common.graph            | AbstractBaseGraph.java                              | src      |       34 |       22 |       20 |      197 |      273 |
| common.graph            | AbstractDirectedNetworkConnections.java             | src      |       18 |        7 |       17 |       92 |      134 |
| common.graph            | AbstractGraph.java                                  | src      |        7 |        8 |       15 |       35 |       65 |
| common.graph            | AbstractGraphBuilder.java                           | src      |        4 |       11 |       15 |       11 |       41 |
| common.graph            | AbstractNetwork.java                                | src      |       36 |       16 |       20 |      220 |      292 |
| common.graph            | AbstractUndirectedNetworkConnections.java           | src      |       15 |        7 |       15 |       60 |       97 |
| common.graph            | AbstractValueGraph.java                             | src      |       21 |       12 |       15 |      108 |      156 |
| common.graph            | BaseGraph.java                                      | src      |       19 |       96 |       24 |       23 |      162 |
| common.graph            | ConfigurableMutableGraph.java                       | src      |       11 |       10 |       15 |       39 |       75 |
| common.graph            | ConfigurableMutableNetwork.java                     | src      |       20 |       18 |       17 |      118 |      173 |
| common.graph            | ConfigurableMutableValueGraph.java                  | src      |       22 |       18 |       16 |      117 |      173 |
| common.graph            | ConfigurableNetwork.java                            | src      |       27 |       23 |       19 |      133 |      202 |
| common.graph            | ConfigurableValueGraph.java                         | src      |       24 |       21 |       16 |      107 |      168 |
| common.graph            | DirectedGraphConnections.java                       | src      |       25 |       11 |       20 |      195 |      251 |
| common.graph            | DirectedMultiNetworkConnections.java                | src      |       19 |        7 |       15 |      104 |      145 |
| common.graph            | DirectedNetworkConnections.java                     | src      |       10 |        7 |       15 |       35 |       67 |
| common.graph            | EdgesConnecting.java                                | src      |       10 |        9 |       15 |       35 |       69 |
| common.graph            | ElementOrder.java                                   | src      |       19 |       35 |       15 |       94 |      163 |
| common.graph            | EndpointPair.java                                   | src      |       36 |       48 |       25 |      143 |      252 |
| common.graph            | EndpointPairIterator.java                           | src      |       13 |       39 |       16 |       68 |      136 |
| common.graph            | ForwardingGraph.java                                | src      |       17 |       10 |       15 |       57 |       99 |
| common.graph            | ForwardingNetwork.java                              | src      |       30 |        7 |       15 |      110 |      162 |
| common.graph            | ForwardingValueGraph.java                           | src      |       21 |       11 |       15 |       75 |      122 |
| common.graph            | Graph.java                                          | src      |       22 |      194 |       27 |       42 |      285 |
| common.graph            | GraphBuilder.java                                   | src      |       13 |       43 |       15 |       47 |      118 |
| common.graph            | GraphConnections.java                               | src      |       11 |       25 |       15 |       18 |       69 |
| common.graph            | GraphConstants.java                                 | src      |        8 |        0 |       17 |       34 |       59 |
| common.graph            | Graphs.java                                         | src      |       80 |       92 |       34 |      421 |      627 |
| common.graph            | ImmutableGraph.java                                 | src      |       10 |       20 |       18 |       50 |       98 |
| common.graph            | ImmutableNetwork.java                               | src      |       14 |       21 |       21 |       85 |      141 |
| common.graph            | ImmutableValueGraph.java                            | src      |       10 |       19 |       18 |       52 |       99 |
| common.graph            | MapIteratorCache.java                               | src      |       21 |       14 |       20 |       79 |      134 |
| common.graph            | MapRetrievalCache.java                              | src      |       15 |        6 |       22 |       62 |      105 |
| common.graph            | MultiEdgesConnecting.java                           | src      |        8 |        9 |       15 |       37 |       69 |
| common.graph            | MutableGraph.java                                   | src      |        9 |       64 |       15 |       18 |      106 |
| common.graph            | MutableNetwork.java                                 | src      |        8 |       73 |       15 |       16 |      112 |
| common.graph            | MutableValueGraph.java                              | src      |        9 |       72 |       15 |       18 |      114 |
| common.graph            | Network.java                                        | src      |       35 |      314 |       27 |       47 |      423 |
| common.graph            | NetworkBuilder.java                                 | src      |       16 |       59 |       15 |       66 |      156 |
| common.graph            | NetworkConnections.java                             | src      |       15 |       25 |       15 |       21 |       76 |
| common.graph            | PredecessorsFunction.java                           | src      |        4 |       78 |       15 |        6 |      103 |
| common.graph            | SuccessorsFunction.java                             | src      |        4 |       81 |       15 |        6 |      106 |
| common.graph            | Traverser.java                                      | src      |       56 |      250 |       22 |      334 |      662 |
| common.graph            | UndirectedGraphConnections.java                     | src      |       15 |        7 |       15 |       55 |       92 |
| common.graph            | UndirectedMultiNetworkConnections.java              | src      |       16 |        7 |       15 |       83 |      121 |
| common.graph            | UndirectedNetworkConnections.java                   | src      |        9 |        7 |       15 |       27 |       58 |
| common.graph            | ValueGraph.java                                     | src      |       27 |      245 |       27 |       50 |      349 |
| common.graph            | ValueGraphBuilder.java                              | src      |       13 |       52 |       15 |       46 |      126 |
| common.graph            | package-info.java                                   | src      |        2 |        7 |       15 |        5 |       29 |
+-------------------------+-----------------------------------------------------+----------+----------+----------+----------+----------+----------+
| common.hash             | AbstractByteHasher.java                             | src      |       17 |        6 |       13 |       88 |      124 |
| common.hash             | AbstractCompositeHashFunction.java                  | src      |       25 |       12 |       14 |      144 |      195 |
| common.hash             | AbstractHashFunction.java                           | src      |       12 |        5 |       13 |       49 |       79 |
| common.hash             | AbstractHasher.java                                 | src      |       15 |        6 |       13 |       87 |      121 |
| common.hash             | AbstractNonStreamingHashFunction.java               | src      |       20 |        7 |       14 |       92 |      133 |
| common.hash             | AbstractStreamingHasher.java                        | src      |       27 |       34 |       34 |      119 |      214 |
| common.hash             | BloomFilter.java                                    | src      |       43 |      281 |       39 |      255 |      618 |
| common.hash             | BloomFilterStrategies.java                          | src      |       29 |       51 |       21 |      183 |      284 |
| common.hash             | ChecksumHashFunction.java                           | src      |       14 |        5 |       18 |       56 |       93 |
| common.hash             | Crc32cHashFunction.java                             | src      |       11 |        6 |       15 |       96 |      128 |
| common.hash             | FarmHashFingerprint64.java                          | src      |       19 |       22 |       21 |      154 |      216 |
| common.hash             | Funnel.java                                         | src      |        4 |       29 |       13 |        7 |       53 |
| common.hash             | Funnels.java                                        | src      |       45 |       45 |       13 |      160 |      263 |
| common.hash             | HashCode.java                                       | src      |       56 |      104 |       19 |      244 |      423 |
| common.hash             | HashFunction.java                                   | src      |       13 |      176 |       13 |       21 |      223 |
| common.hash             | Hasher.java                                         | src      |       18 |       64 |       13 |       44 |      139 |
| common.hash             | Hashing.java                                        | src      |       57 |      330 |       17 |      274 |      678 |
| common.hash             | HashingInputStream.java                             | src      |       11 |       34 |       13 |       47 |      105 |
| common.hash             | HashingOutputStream.java                            | src      |        9 |       16 |       20 |       31 |       76 |
| common.hash             | ImmutableSupplier.java                              | src      |        3 |        4 |       13 |        5 |       25 |
| common.hash             | LittleEndianByteArray.java                          | src      |       24 |       60 |       41 |      135 |      260 |
| common.hash             | LongAddable.java                                    | src      |        4 |        5 |       13 |        6 |       28 |
| common.hash             | LongAddables.java                                   | src      |        8 |        5 |       13 |       45 |       71 |
| common.hash             | LongAdder.java                                      | src      |       19 |       57 |        9 |       97 |      182 |
| common.hash             | MacHashFunction.java                                | src      |       19 |        5 |       14 |      101 |      139 |
| common.hash             | MessageDigestHashFunction.java                      | src      |       24 |        6 |       14 |      119 |      163 |
| common.hash             | Murmur3_128HashFunction.java                        | src      |       31 |        7 |       23 |      154 |      215 |
| common.hash             | Murmur3_32HashFunction.java                         | src      |       55 |        9 |       30 |      317 |      411 |
| common.hash             | PrimitiveSink.java                                  | src      |       15 |       55 |       13 |       29 |      112 |
| common.hash             | SipHashFunction.java                                | src      |       26 |       13 |       32 |      113 |      184 |
| common.hash             | Striped64.java                                      | src      |       24 |       44 |       73 |      169 |      310 |
| common.hash             | package-info.java                                   | src      |        2 |        6 |       14 |        5 |       27 |
+-------------------------+-----------------------------------------------------+----------+----------+----------+----------+----------+----------+
| common.html             | HtmlEscapers.java                                   | src      |        6 |       28 |       16 |       21 |       71 |
| common.html             | package-info.java                                   | src      |        2 |        8 |       13 |        5 |       28 |
+-------------------------+-----------------------------------------------------+----------+----------+----------+----------+----------+----------+
| common.io               | AppendableWriter.java                               | src      |       17 |       13 |       22 |       73 |      125 |
| common.io               | BaseEncoding.java                                   | src      |      124 |      236 |       24 |      776 |     1160 |
| common.io               | ByteArrayDataInput.java                             | src      |       17 |       12 |       15 |       50 |       94 |
| common.io               | ByteArrayDataOutput.java                            | src      |       17 |       11 |       13 |       38 |       79 |
| common.io               | ByteProcessor.java                                  | src      |        4 |       18 |       13 |       13 |       48 |
| common.io               | ByteSink.java                                       | src      |       17 |       59 |       13 |       68 |      157 |
| common.io               | ByteSource.java                                     | src      |       79 |      202 |       38 |      420 |      739 |
| common.io               | ByteStreams.java                                    | src      |       94 |      197 |       31 |      584 |      906 |
| common.io               | CharSequenceReader.java                             | src      |       18 |        6 |       14 |       92 |      130 |
| common.io               | CharSink.java                                       | src      |       17 |       83 |       13 |       77 |      190 |
| common.io               | CharSource.java                                     | src      |       67 |      265 |       15 |      376 |      723 |
| common.io               | CharStreams.java                                    | src      |       33 |      115 |       20 |      179 |      347 |
| common.io               | Closeables.java                                     | src      |        7 |       64 |       13 |       46 |      130 |
| common.io               | Closer.java                                         | src      |       27 |      120 |       20 |      125 |      292 |
| common.io               | CountingInputStream.java                            | src      |       13 |       11 |       14 |       58 |       96 |
| common.io               | CountingOutputStream.java                           | src      |       10 |       11 |       16 |       33 |       70 |
| common.io               | FileBackedOutputStream.java                         | src      |       22 |       38 |       16 |      136 |      212 |
| common.io               | FileWriteMode.java                                  | src      |        3 |        6 |       13 |        7 |       29 |
| common.io               | Files.java                                          | src      |       74 |      408 |       30 |      391 |      903 |
| common.io               | Flushables.java                                     | src      |        6 |       25 |       13 |       31 |       75 |
| common.io               | InsecureRecursiveDeleteException.java               | src      |        4 |       12 |       15 |       15 |       46 |
| common.io               | LineBuffer.java                                     | src      |       10 |       34 |       15 |       56 |      115 |
| common.io               | LineProcessor.java                                  | src      |        5 |       15 |       13 |       13 |       46 |
| common.io               | LineReader.java                                     | src      |        7 |       17 |       15 |       46 |       85 |
| common.io               | LittleEndianDataInputStream.java                    | src      |       26 |       80 |       13 |      118 |      237 |
| common.io               | LittleEndianDataOutputStream.java                   | src      |       17 |       62 |       17 |       77 |      173 |
| common.io               | MoreFiles.java                                      | src      |       75 |      215 |       78 |      426 |      794 |
| common.io               | MultiInputStream.java                               | src      |       13 |       12 |       13 |       80 |      118 |
| common.io               | MultiReader.java                                    | src      |        9 |        6 |       13 |       63 |       91 |
| common.io               | PatternFilenameFilter.java                          | src      |        7 |       18 |       13 |       24 |       62 |
| common.io               | ReaderInputStream.java                              | src      |       25 |       47 |       37 |      146 |      255 |
| common.io               | RecursiveDeleteOption.java                          | src      |        3 |       19 |       15 |       11 |       48 |
| common.io               | Resources.java                                      | src      |       20 |       87 |       17 |       87 |      211 |
| common.io               | package-info.java                                   | src      |        2 |       16 |       13 |        5 |       36 |
+-------------------------+-----------------------------------------------------+----------+----------+----------+----------+----------+----------+
| common.math             | BigIntegerMath.java                                 | src      |       43 |       74 |       81 |      276 |      474 |
| common.math             | DoubleMath.java                                     | src      |       34 |      191 |       39 |      264 |      528 |
| common.math             | DoubleUtils.java                                    | src      |       20 |        5 |       44 |       73 |      142 |
| common.math             | IntMath.java                                        | src      |       48 |      179 |       84 |      417 |      728 |
| common.math             | LinearTransformation.java                           | src      |       50 |       67 |       13 |      174 |      304 |
| common.math             | LongMath.java                                       | src      |       71 |      193 |      183 |      760 |     1207 |
| common.math             | MathPreconditions.java                              | src      |       14 |        5 |       13 |       74 |      106 |
| common.math             | PairedStats.java                                    | src      |       24 |      127 |       16 |      153 |      320 |
| common.math             | PairedStatsAccumulator.java                         | src      |       18 |       85 |       31 |      113 |      247 |
| common.math             | Quantiles.java                                      | src      |       54 |      273 |       66 |      292 |      685 |
| common.math             | Stats.java                                          | src      |       36 |      292 |       18 |      240 |      586 |
| common.math             | StatsAccumulator.java                               | src      |       25 |      165 |       34 |      150 |      374 |
| common.math             | package-info.java                                   | src      |        2 |        9 |       13 |        5 |       29 |
+-------------------------+-----------------------------------------------------+----------+----------+----------+----------+----------+----------+
| common.net              | HostAndPort.java                                    | src      |       27 |      112 |       18 |      161 |      318 |
| common.net              | HostSpecifier.java                                  | src      |       20 |       53 |       25 |       70 |      168 |
| common.net              | HttpHeaders.java                                    | src      |       15 |      192 |       17 |      198 |      422 |
| common.net              | InetAddresses.java                                  | src      |       90 |      387 |       50 |      462 |      989 |
| common.net              | InternetDomainName.java                             | src      |       70 |      327 |       38 |      202 |      637 |
| common.net              | MediaType.java                                      | src      |      118 |      410 |       35 |      475 |     1038 |
| common.net              | PercentEscaper.java                                 | src      |       13 |       52 |       39 |      129 |      233 |
| common.net              | UrlEscapers.java                                    | src      |       12 |       94 |       15 |       27 |      148 |
| common.net              | package-info.java                                   | src      |        2 |        9 |       13 |        3 |       27 |
+-------------------------+-----------------------------------------------------+----------+----------+----------+----------+----------+----------+
| common.primitives       | Booleans.java                                       | src      |       50 |      178 |       21 |      304 |      553 |
| common.primitives       | Bytes.java                                          | src      |       35 |      115 |       22 |      226 |      398 |
| common.primitives       | Chars.java                                          | src      |       53 |      235 |       20 |      330 |      638 |
| common.primitives       | Doubles.java                                        | src      |       60 |      254 |       33 |      367 |      714 |
| common.primitives       | Floats.java                                         | src      |       57 |      245 |       26 |      338 |      666 |
| common.primitives       | ImmutableDoubleArray.java                           | src      |       70 |      164 |       38 |      358 |      630 |
| common.primitives       | ImmutableIntArray.java                              | src      |       69 |      162 |       37 |      352 |      620 |
| common.primitives       | ImmutableLongArray.java                             | src      |       69 |      162 |       37 |      354 |      622 |
| common.primitives       | Ints.java                                           | src      |       64 |      286 |       21 |      371 |      742 |
| common.primitives       | Longs.java                                          | src      |       69 |      276 |       27 |      417 |      789 |
| common.primitives       | ParseRequest.java                                   | src      |        7 |        0 |       14 |       34 |       55 |
| common.primitives       | Primitives.java                                     | src      |       18 |       45 |       16 |       59 |      138 |
| common.primitives       | Shorts.java                                         | src      |       61 |      249 |       21 |      354 |      685 |
| common.primitives       | SignedBytes.java                                    | src      |       18 |       91 |       18 |       92 |      219 |
| common.primitives       | UnsignedBytes.java                                  | src      |       45 |      174 |       48 |      243 |      510 |
| common.primitives       | UnsignedInteger.java                                | src      |       26 |      104 |       15 |      106 |      251 |
| common.primitives       | UnsignedInts.java                                   | src      |       30 |      186 |       14 |      160 |      390 |
| common.primitives       | UnsignedLong.java                                   | src      |       28 |      102 |       14 |      119 |      263 |
| common.primitives       | UnsignedLongs.java                                  | src      |       38 |      179 |       42 |      241 |      500 |
| common.primitives       | package-info.java                                   | src      |        2 |       48 |       13 |        5 |       68 |
+-------------------------+-----------------------------------------------------+----------+----------+----------+----------+----------+----------+
| common.reflect          | AbstractInvocationHandler.java                      | src      |       10 |       60 |       18 |       57 |      145 |
| common.reflect          | ClassPath.java                                      | src      |       54 |      139 |       26 |      372 |      591 |
| common.reflect          | Element.java                                        | src      |       31 |       13 |       13 |      121 |      178 |
| common.reflect          | ImmutableTypeToInstanceMap.java                     | src      |       20 |       60 |       13 |       80 |      173 |
| common.reflect          | Invokable.java                                      | src      |       51 |       65 |       26 |      239 |      381 |
| common.reflect          | MutableTypeToInstanceMap.java                       | src      |       25 |       18 |       13 |      108 |      164 |
| common.reflect          | Parameter.java                                      | src      |       19 |        6 |       18 |       93 |      136 |
| common.reflect          | Reflection.java                                     | src      |        9 |       35 |       13 |       34 |       91 |
| common.reflect          | TypeCapture.java                                    | src      |        5 |        5 |       13 |       12 |       35 |
| common.reflect          | TypeParameter.java                                  | src      |        9 |       13 |       13 |       31 |       66 |
| common.reflect          | TypeResolver.java                                   | src      |       56 |       85 |       70 |      394 |      605 |
| common.reflect          | TypeToInstanceMap.java                              | src      |        7 |       49 |       13 |       14 |       83 |
| common.reflect          | TypeToken.java                                      | src      |      138 |      326 |       84 |      893 |     1441 |
| common.reflect          | TypeVisitor.java                                    | src      |       10 |       35 |       14 |       44 |      103 |
| common.reflect          | Types.java                                          | src      |       86 |       52 |       24 |      512 |      674 |
| common.reflect          | package-info.java                                   | src      |        2 |        4 |       13 |        5 |       24 |
+-------------------------+-----------------------------------------------------+----------+----------+----------+----------+----------+----------+
| common.util.concurrent  | AbstractCatchingFuture.java                         | src      |       23 |        8 |       19 |      164 |      214 |
| common.util.concurrent  | AbstractCheckedFuture.java                          | src      |        6 |       53 |       15 |       44 |      118 |
| common.util.concurrent  | AbstractExecutionThreadService.java                 | src      |       25 |       56 |       19 |      143 |      243 |
| common.util.concurrent  | AbstractFuture.java                                 | src      |       99 |      230 |      164 |      861 |     1354 |
| common.util.concurrent  | AbstractIdleService.java                            | src      |       25 |       21 |       13 |      136 |      195 |
| common.util.concurrent  | AbstractListeningExecutorService.java               | src      |        8 |       11 |       13 |       36 |       68 |
| common.util.concurrent  | AbstractScheduledService.java                       | src      |       54 |      161 |       49 |      334 |      598 |
| common.util.concurrent  | AbstractService.java                                | src      |       61 |       92 |       30 |      436 |      619 |
| common.util.concurrent  | AbstractTransformFuture.java                        | src      |       22 |        8 |       74 |      146 |      250 |
| common.util.concurrent  | AggregateFuture.java                                | src      |       30 |       33 |       50 |      191 |      304 |
| common.util.concurrent  | AggregateFutureState.java                           | src      |       22 |        9 |       48 |       98 |      177 |
| common.util.concurrent  | AsyncCallable.java                                  | src      |        3 |       16 |       13 |       10 |       42 |
| common.util.concurrent  | AsyncFunction.java                                  | src      |        3 |       14 |       13 |        9 |       39 |
| common.util.concurrent  | AtomicDouble.java                                   | src      |       26 |      113 |       13 |       93 |      245 |
| common.util.concurrent  | AtomicDoubleArray.java                              | src      |       24 |      113 |       16 |      105 |      258 |
| common.util.concurrent  | AtomicLongMap.java                                  | src      |       35 |      133 |       15 |      162 |      345 |
| common.util.concurrent  | Atomics.java                                        | src      |        7 |       30 |       13 |       21 |       71 |
| common.util.concurrent  | Callables.java                                      | src      |        9 |       32 |       16 |       85 |      142 |
| common.util.concurrent  | CheckedFuture.java                                  | src      |        5 |       55 |       14 |       17 |       91 |
| common.util.concurrent  | CollectionFuture.java                               | src      |       16 |        0 |       17 |       78 |      111 |
| common.util.concurrent  | CombinedFuture.java                                 | src      |       27 |        0 |       13 |      153 |      193 |
| common.util.concurrent  | CycleDetectingLockFactory.java                      | src      |       94 |      330 |       61 |      484 |      969 |
| common.util.concurrent  | DirectExecutor.java                                 | src      |        5 |        4 |       13 |       15 |       37 |
| common.util.concurrent  | ExecutionError.java                                 | src      |        7 |       10 |       13 |       21 |       51 |
| common.util.concurrent  | ExecutionList.java                                  | src      |       14 |       43 |       35 |       72 |      164 |
| common.util.concurrent  | ExecutionSequencer.java                             | src      |       17 |       25 |       38 |       87 |      167 |
| common.util.concurrent  | FakeTimeLimiter.java                                | src      |        8 |       10 |       17 |       62 |       97 |
| common.util.concurrent  | FluentFuture.java                                   | src      |       17 |      265 |       13 |       78 |      373 |
| common.util.concurrent  | ForwardingBlockingDeque.java                        | src      |       20 |       21 |       15 |       74 |      130 |
| common.util.concurrent  | ForwardingBlockingQueue.java                        | src      |       12 |       14 |       13 |       44 |       83 |
| common.util.concurrent  | ForwardingCheckedFuture.java                        | src      |        9 |       34 |       15 |       38 |       96 |
| common.util.concurrent  | ForwardingCondition.java                            | src      |       10 |        0 |       13 |       36 |       59 |
| common.util.concurrent  | ForwardingExecutorService.java                      | src      |       17 |        8 |       13 |       77 |      115 |
| common.util.concurrent  | ForwardingFluentFuture.java                         | src      |       11 |       10 |       13 |       39 |       73 |
| common.util.concurrent  | ForwardingFuture.java                               | src      |       12 |       16 |       14 |       48 |       90 |
| common.util.concurrent  | ForwardingListenableFuture.java                     | src      |        9 |       16 |       14 |       29 |       68 |
| common.util.concurrent  | ForwardingListeningExecutorService.java             | src      |        7 |        9 |       13 |       25 |       54 |
| common.util.concurrent  | ForwardingLock.java                                 | src      |        9 |        0 |       13 |       32 |       54 |
| common.util.concurrent  | FutureCallback.java                                 | src      |        4 |       15 |       13 |       11 |       43 |
| common.util.concurrent  | Futures.java                                        | src      |       74 |      658 |      108 |      457 |     1297 |
| common.util.concurrent  | FuturesGetChecked.java                              | src      |       31 |       10 |       42 |      219 |      302 |
| common.util.concurrent  | GwtFluentFutureCatchingSpecialization.java          | src      |        3 |        5 |       18 |        5 |       31 |
| common.util.concurrent  | GwtFuturesCatchingSpecialization.java               | src      |        3 |        6 |       18 |        5 |       32 |
| common.util.concurrent  | ImmediateFuture.java                                | src      |       28 |        0 |       19 |      123 |      170 |
| common.util.concurrent  | InterruptibleTask.java                              | src      |       12 |       16 |       75 |       96 |      199 |
| common.util.concurrent  | JdkFutureAdapters.java                              | src      |       18 |       50 |       29 |       74 |      171 |
| common.util.concurrent  | ListenableFuture.java                               | src      |        3 |      121 |       13 |        7 |      144 |
| common.util.concurrent  | ListenableFutureTask.java                           | src      |       10 |       30 |       17 |       31 |       88 |
| common.util.concurrent  | ListenableScheduledFuture.java                      | src      |        3 |        6 |       13 |        7 |       29 |
| common.util.concurrent  | ListenerCallQueue.java                              | src      |       20 |       58 |       22 |      125 |      225 |
| common.util.concurrent  | ListeningExecutorService.java                       | src      |        7 |       57 |       13 |       25 |      102 |
| common.util.concurrent  | ListeningScheduledExecutorService.java              | src      |        7 |        9 |       13 |       25 |       54 |
| common.util.concurrent  | Monitor.java                                        | src      |       71 |      397 |      122 |      551 |     1141 |
| common.util.concurrent  | MoreExecutors.java                                  | src      |       77 |      278 |       69 |      572 |      996 |
| common.util.concurrent  | Partially.java                                      | src      |        4 |       15 |       13 |       17 |       49 |
| common.util.concurrent  | Platform.java                                       | src      |        4 |        0 |       13 |       12 |       29 |
| common.util.concurrent  | RateLimiter.java                                    | src      |       34 |      208 |       36 |      154 |      432 |
| common.util.concurrent  | Runnables.java                                      | src      |        6 |        5 |       13 |       17 |       41 |
| common.util.concurrent  | SequentialExecutor.java                             | src      |       18 |       38 |       48 |      138 |      242 |
| common.util.concurrent  | Service.java                                        | src      |       24 |      175 |       13 |       80 |      292 |
| common.util.concurrent  | ServiceManager.java                                 | src      |       68 |      259 |       44 |      493 |      864 |
| common.util.concurrent  | SettableFuture.java                                 | src      |        7 |       17 |       13 |       28 |       65 |
| common.util.concurrent  | SimpleTimeLimiter.java                              | src      |       32 |       19 |       16 |      222 |      289 |
| common.util.concurrent  | SmoothRateLimiter.java                              | src      |       30 |       91 |      133 |      137 |      391 |
| common.util.concurrent  | Striped.java                                        | src      |       60 |      150 |       55 |      320 |      585 |
| common.util.concurrent  | ThreadFactoryBuilder.java                           | src      |       13 |       63 |       17 |       91 |      184 |
| common.util.concurrent  | TimeLimiter.java                                    | src      |        8 |      116 |       13 |       25 |      162 |
| common.util.concurrent  | TimeoutFuture.java                                  | src      |       17 |        7 |       54 |      103 |      181 |
| common.util.concurrent  | TrustedListenableFutureTask.java                    | src      |       27 |       15 |       24 |      114 |      180 |
| common.util.concurrent  | UncaughtExceptionHandlers.java                      | src      |        9 |       22 |       16 |       34 |       81 |
| common.util.concurrent  | UncheckedExecutionException.java                    | src      |        7 |       15 |       13 |       21 |       56 |
| common.util.concurrent  | UncheckedTimeoutException.java                      | src      |        7 |        6 |       13 |       17 |       43 |
| common.util.concurrent  | Uninterruptibles.java                               | src      |       23 |       77 |       22 |      252 |      374 |
| common.util.concurrent  | WrappingExecutorService.java                        | src      |       22 |       23 |       15 |      102 |      162 |
| common.util.concurrent  | WrappingScheduledExecutorService.java               | src      |        8 |        8 |       13 |       35 |       64 |
| common.util.concurrent  | package-info.java                                   | src      |        2 |       13 |       13 |        5 |       33 |
+-------------------------+-----------------------------------------------------+----------+----------+----------+----------+----------+----------+
| common.xml              | XmlEscapers.java                                    | src      |       11 |       62 |       30 |       42 |      145 |
| common.xml              | package-info.java                                   | src      |        2 |        8 |       13 |        5 |       28 |
+-------------------------+-----------------------------------------------------+----------+----------+----------+----------+----------+----------+
| thirdparty.publicsuffix | PublicSuffixPatterns.java                           | src      |        7 |       24 |       16 |       16 |       63 |
| thirdparty.publicsuffix | PublicSuffixType.java                               | src      |       11 |        8 |       13 |       37 |       69 |
| thirdparty.publicsuffix | TrieParser.java                                     | src      |       11 |       14 |       20 |       57 |      102 |
+-------------------------+-----------------------------------------------------+----------+----------+----------+----------+----------+----------+
| 18 package(s)           | 573 file(s)                                         | java     |    17113 |    43662 |    12878 |    88070 |   161723 |
+-------------------------+-----------------------------------------------------+----------+----------+----------+----------+----------+----------+

[INFO] 
[INFO] -------------------< com.google.guava:guava-testlib >-------------------
[INFO] Building Guava Testing Library HEAD-jre-SNAPSHOT                   [3/5]
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- sloc-maven-plugin:0.1.3:sloc (default-cli) @ guava-testlib ---
[INFO] SLOC - directory: /home/orhanku/ME/DEV/x/guava/guava-testlib/src
+--------------------------+---------------------------------------------+----------+----------+----------+----------+----------+----------+
| Package Name             | File Name                                   | Type     | Blank    | JavaDoc  | Comment  | Code     | Total    |
+--------------------------+---------------------------------------------+----------+----------+----------+----------+----------+----------+
| collect.testing.features | CollectionFeature.java                      | src      |       18 |       47 |       16 |       47 |      128 |
| collect.testing.features | CollectionSize.java                         | src      |       11 |       16 |       22 |       48 |       97 |
| collect.testing.features | ConflictingRequirementsException.java       | src      |        8 |        5 |       15 |       25 |       53 |
| collect.testing.features | Feature.java                                | src      |        3 |        6 |       15 |        8 |       32 |
| collect.testing.features | FeatureUtil.java                            | src      |       19 |       83 |       16 |      180 |      298 |
| collect.testing.features | ListFeature.java                            | src      |       10 |        5 |       16 |       37 |       68 |
| collect.testing.features | MapFeature.java                             | src      |        9 |       29 |       16 |       41 |       95 |
| collect.testing.features | SetFeature.java                             | src      |        8 |        5 |       16 |       27 |       56 |
| collect.testing.features | TesterAnnotation.java                       | src      |        3 |        9 |       13 |       11 |       36 |
| collect.testing.features | TesterRequirements.java                     | src      |       12 |        6 |       15 |       47 |       80 |
+--------------------------+---------------------------------------------+----------+----------+----------+----------+----------+----------+
| collect.testing.google   | AbstractBiMapTester.java                    | src      |        8 |        0 |       19 |       56 |       83 |
| collect.testing.google   | AbstractListMultimapTester.java             | src      |        8 |        5 |       13 |       28 |       54 |
| collect.testing.google   | AbstractMultimapTester.java                 | src      |       36 |        9 |       16 |      138 |      199 |
| collect.testing.google   | AbstractMultisetSetCountTester.java         | src      |       55 |       20 |       32 |      293 |      400 |
| collect.testing.google   | AbstractMultisetTester.java                 | src      |        4 |        5 |       15 |       15 |       39 |
| collect.testing.google   | BiMapClearTester.java                       | src      |        9 |        5 |       15 |       52 |       81 |
| collect.testing.google   | BiMapEntrySetTester.java                    | src      |        7 |        0 |       15 |       61 |       83 |
| collect.testing.google   | BiMapGenerators.java                        | src      |        5 |        6 |       15 |       37 |       63 |
| collect.testing.google   | BiMapInverseTester.java                     | src      |       11 |       12 |       15 |       46 |       84 |
| collect.testing.google   | BiMapPutTester.java                         | src      |       22 |        0 |       20 |      112 |      154 |
| collect.testing.google   | BiMapRemoveTester.java                      | src      |       10 |        5 |       15 |       66 |       96 |
| collect.testing.google   | BiMapTestSuiteBuilder.java                  | src      |       19 |        6 |       24 |      108 |      157 |
| collect.testing.google   | DerivedGoogleCollectionGenerators.java      | src      |       34 |        6 |       17 |      154 |      211 |
| collect.testing.google   | GoogleHelpers.java                          | src      |        6 |        5 |       15 |       13 |       39 |
| collect.testing.google   | ListGenerators.java                         | src      |       18 |        5 |       15 |      105 |      143 |
| collect.testing.google   | ListMultimapAsMapTester.java                | src      |        9 |        7 |       13 |       69 |       98 |
| collect.testing.google   | ListMultimapEqualsTester.java               | src      |        4 |        5 |       13 |       28 |       50 |
| collect.testing.google   | ListMultimapPutAllTester.java               | src      |        8 |        5 |       13 |       25 |       51 |
| collect.testing.google   | ListMultimapPutTester.java                  | src      |       13 |        5 |       14 |       46 |       78 |
| collect.testing.google   | ListMultimapRemoveTester.java               | src      |       17 |        5 |       13 |       70 |      105 |
| collect.testing.google   | ListMultimapReplaceValuesTester.java        | src      |        5 |        5 |       13 |       23 |       46 |
| collect.testing.google   | ListMultimapTestSuiteBuilder.java           | src      |       12 |        6 |       15 |      104 |      137 |
| collect.testing.google   | MapGenerators.java                          | src      |       27 |        5 |       18 |      189 |      239 |
| collect.testing.google   | MultimapAsMapGetTester.java                 | src      |       17 |        5 |       15 |      100 |      137 |
| collect.testing.google   | MultimapAsMapTester.java                    | src      |       15 |        5 |       13 |      109 |      142 |
| collect.testing.google   | MultimapClearTester.java                    | src      |       15 |        5 |       15 |      103 |      138 |
| collect.testing.google   | MultimapContainsEntryTester.java            | src      |       10 |        5 |       17 |       56 |       88 |
| collect.testing.google   | MultimapContainsKeyTester.java              | src      |       12 |        5 |       16 |       58 |       91 |
| collect.testing.google   | MultimapContainsValueTester.java            | src      |        8 |        5 |       16 |       39 |       68 |
| collect.testing.google   | MultimapEntriesTester.java                  | src      |       13 |        5 |       13 |       91 |      122 |
| collect.testing.google   | MultimapEqualsTester.java                   | src      |        9 |        5 |       13 |       65 |       92 |
| collect.testing.google   | MultimapFeature.java                        | src      |        8 |        5 |       16 |       30 |       59 |
| collect.testing.google   | MultimapForEachTester.java                  | src      |        4 |        5 |       15 |       26 |       50 |
| collect.testing.google   | MultimapGetTester.java                      | src      |       19 |        5 |       16 |      118 |      158 |
| collect.testing.google   | MultimapKeySetTester.java                   | src      |        8 |        5 |       13 |       53 |       79 |
| collect.testing.google   | MultimapKeysTester.java                     | src      |       10 |        5 |       13 |       79 |      107 |
| collect.testing.google   | MultimapPutAllMultimapTester.java           | src      |       11 |        5 |       15 |       80 |      111 |
| collect.testing.google   | MultimapPutIterableTester.java              | src      |       26 |        5 |       24 |      166 |      221 |
| collect.testing.google   | MultimapPutTester.java                      | src      |       40 |        5 |       15 |      162 |      222 |
| collect.testing.google   | MultimapRemoveAllTester.java                | src      |       14 |        5 |       15 |       59 |       93 |
| collect.testing.google   | MultimapRemoveEntryTester.java              | src      |       29 |        5 |       17 |      140 |      191 |
| collect.testing.google   | MultimapReplaceValuesTester.java            | src      |       15 |        5 |       17 |      107 |      144 |
| collect.testing.google   | MultimapSizeTester.java                     | src      |       15 |        5 |       15 |       71 |      106 |
| collect.testing.google   | MultimapTestSuiteBuilder.java               | src      |       78 |        6 |       23 |      568 |      675 |
| collect.testing.google   | MultimapToStringTester.java                 | src      |        8 |        5 |       13 |       44 |       70 |
| collect.testing.google   | MultimapValuesTester.java                   | src      |        6 |        5 |       13 |       42 |       66 |
| collect.testing.google   | MultisetAddTester.java                      | src      |       15 |        5 |       15 |       92 |      127 |
| collect.testing.google   | MultisetContainsTester.java                 | src      |        5 |        5 |       13 |       18 |       41 |
| collect.testing.google   | MultisetCountTester.java                    | src      |       12 |        9 |       15 |       57 |       93 |
| collect.testing.google   | MultisetElementSetTester.java               | src      |       10 |        9 |       15 |       74 |      108 |
| collect.testing.google   | MultisetEntrySetTester.java                 | src      |       21 |        5 |       15 |      214 |      255 |
| collect.testing.google   | MultisetEqualsTester.java                   | src      |        7 |        5 |       13 |       38 |       63 |
| collect.testing.google   | MultisetFeature.java                        | src      |        6 |        9 |       15 |       25 |       55 |
| collect.testing.google   | MultisetForEachEntryTester.java             | src      |        7 |        9 |       15 |       46 |       77 |
| collect.testing.google   | MultisetIteratorTester.java                 | src      |        8 |       10 |       13 |       83 |      114 |
| collect.testing.google   | MultisetNavigationTester.java               | src      |       83 |        5 |       15 |      571 |      674 |
| collect.testing.google   | MultisetReadsTester.java                    | src      |       16 |        6 |       15 |       77 |      114 |
| collect.testing.google   | MultisetRemoveTester.java                   | src      |       19 |       10 |       16 |      151 |      196 |
| collect.testing.google   | MultisetSerializationTester.java            | src      |        5 |        6 |       15 |       22 |       48 |
| collect.testing.google   | MultisetSetCountConditionallyTester.java    | src      |       15 |        6 |       22 |       68 |      111 |
| collect.testing.google   | MultisetSetCountUnconditionallyTester.java  | src      |        5 |        6 |       15 |       21 |       47 |
| collect.testing.google   | MultisetTestSuiteBuilder.java               | src      |       34 |        7 |       16 |      225 |      282 |
| collect.testing.google   | SetGenerators.java                          | src      |       46 |        7 |       41 |      330 |      424 |
| collect.testing.google   | SetMultimapAsMapTester.java                 | src      |        9 |        7 |       13 |       68 |       97 |
| collect.testing.google   | SetMultimapEqualsTester.java                | src      |        4 |        5 |       13 |       28 |       50 |
| collect.testing.google   | SetMultimapPutAllTester.java                | src      |        9 |        5 |       13 |       26 |       53 |
| collect.testing.google   | SetMultimapPutTester.java                   | src      |       10 |        5 |       14 |       45 |       74 |
| collect.testing.google   | SetMultimapReplaceValuesTester.java         | src      |        6 |        5 |       13 |       24 |       48 |
| collect.testing.google   | SetMultimapTestSuiteBuilder.java            | src      |       15 |        6 |       15 |      114 |      150 |
| collect.testing.google   | SortedMapGenerators.java                    | src      |       13 |       10 |       15 |       95 |      133 |
| collect.testing.google   | SortedMultisetTestSuiteBuilder.java         | src      |       40 |       20 |       22 |      232 |      314 |
| collect.testing.google   | SortedSetMultimapAsMapTester.java           | src      |        6 |        7 |       13 |       36 |       62 |
| collect.testing.google   | SortedSetMultimapGetTester.java             | src      |        3 |        5 |       15 |       12 |       35 |
| collect.testing.google   | SortedSetMultimapTestSuiteBuilder.java      | src      |        7 |        6 |       15 |       69 |       97 |
| collect.testing.google   | TestBiMapGenerator.java                     | src      |        4 |        5 |       15 |       10 |       34 |
| collect.testing.google   | TestEnumMultisetGenerator.java              | src      |        7 |        5 |       15 |       35 |       62 |
| collect.testing.google   | TestListMultimapGenerator.java              | src      |        3 |        5 |       15 |        6 |       29 |
| collect.testing.google   | TestMultimapGenerator.java                  | src      |        8 |        5 |       15 |       16 |       44 |
| collect.testing.google   | TestMultisetGenerator.java                  | src      |        3 |        5 |       15 |        9 |       32 |
| collect.testing.google   | TestSetMultimapGenerator.java               | src      |        3 |        5 |       15 |        6 |       29 |
| collect.testing.google   | TestStringBiMapGenerator.java               | src      |       10 |        8 |       15 |       50 |       83 |
| collect.testing.google   | TestStringListMultimapGenerator.java        | src      |       13 |        5 |       15 |       64 |       97 |
| collect.testing.google   | TestStringMultisetGenerator.java            | src      |        7 |        5 |       15 |       32 |       59 |
| collect.testing.google   | TestStringSetMultimapGenerator.java         | src      |       12 |        5 |       15 |       64 |       96 |
| collect.testing.google   | UnmodifiableCollectionTests.java            | src      |       56 |       82 |       33 |      244 |      415 |
+--------------------------+---------------------------------------------+----------+----------+----------+----------+----------+----------+
| collect.testing.testers  | AbstractListIndexOfTester.java              | src      |       12 |        5 |       15 |       57 |       89 |
| collect.testing.testers  | AbstractListTester.java                     | src      |        5 |       16 |       21 |       32 |       74 |
| collect.testing.testers  | AbstractQueueTester.java                    | src      |        3 |        5 |       15 |       12 |       35 |
| collect.testing.testers  | AbstractSetTester.java                      | src      |        3 |        0 |       20 |       13 |       36 |
| collect.testing.testers  | CollectionAddAllTester.java                 | src      |       17 |       24 |       20 |      136 |      197 |
| collect.testing.testers  | CollectionAddTester.java                    | src      |       12 |       28 |       16 |       86 |      142 |
| collect.testing.testers  | CollectionClearTester.java                  | src      |        7 |        6 |       20 |       55 |       88 |
| collect.testing.testers  | CollectionContainsAllTester.java            | src      |       13 |        8 |       15 |       71 |      107 |
| collect.testing.testers  | CollectionContainsTester.java               | src      |       10 |        7 |       15 |       48 |       80 |
| collect.testing.testers  | CollectionCreationTester.java               | src      |        7 |       12 |       15 |       36 |       70 |
| collect.testing.testers  | CollectionEqualsTester.java                 | src      |        6 |        5 |       18 |       20 |       49 |
| collect.testing.testers  | CollectionForEachTester.java                | src      |        5 |        6 |       15 |       27 |       53 |
| collect.testing.testers  | CollectionIsEmptyTester.java                | src      |        5 |        6 |       15 |       18 |       44 |
| collect.testing.testers  | CollectionIteratorTester.java               | src      |       14 |        6 |       16 |       99 |      135 |
| collect.testing.testers  | CollectionRemoveAllTester.java              | src      |       19 |        7 |       24 |      157 |      207 |
| collect.testing.testers  | CollectionRemoveIfTester.java               | src      |        9 |        6 |       16 |       75 |      106 |
| collect.testing.testers  | CollectionRemoveTester.java                 | src      |       14 |        6 |       16 |      117 |      153 |
| collect.testing.testers  | CollectionRetainAllTester.java              | src      |       50 |        6 |       38 |      234 |      328 |
| collect.testing.testers  | CollectionSerializationEqualTester.java     | src      |        4 |        6 |       15 |       15 |       40 |
| collect.testing.testers  | CollectionSerializationTester.java          | src      |        4 |        5 |       16 |       17 |       42 |
| collect.testing.testers  | CollectionSizeTester.java                   | src      |        3 |        6 |       15 |       11 |       35 |
| collect.testing.testers  | CollectionSpliteratorTester.java            | src      |       11 |        6 |       17 |       62 |       96 |
| collect.testing.testers  | CollectionStreamTester.java                 | src      |        7 |        6 |       19 |       30 |       62 |
| collect.testing.testers  | CollectionToArrayTester.java                | src      |       21 |       20 |       17 |      143 |      201 |
| collect.testing.testers  | CollectionToStringTester.java               | src      |        8 |        6 |       15 |       45 |       74 |
| collect.testing.testers  | ConcurrentMapPutIfAbsentTester.java         | src      |       13 |        7 |       15 |      101 |      136 |
| collect.testing.testers  | ConcurrentMapRemoveTester.java              | src      |       12 |        6 |       18 |       74 |      110 |
| collect.testing.testers  | ConcurrentMapReplaceEntryTester.java        | src      |       16 |        7 |       20 |      109 |      152 |
| collect.testing.testers  | ConcurrentMapReplaceTester.java             | src      |       11 |        7 |       17 |       73 |      108 |
| collect.testing.testers  | ListAddAllAtIndexTester.java                | src      |       17 |        6 |       19 |      141 |      183 |
| collect.testing.testers  | ListAddAllTester.java                       | src      |        6 |        6 |       15 |       36 |       63 |
| collect.testing.testers  | ListAddAtIndexTester.java                   | src      |       15 |       10 |       20 |      116 |      161 |
| collect.testing.testers  | ListAddTester.java                          | src      |        8 |       10 |       19 |       46 |       83 |
| collect.testing.testers  | ListCreationTester.java                     | src      |        5 |        7 |       15 |       20 |       47 |
| collect.testing.testers  | ListEqualsTester.java                       | src      |       10 |        5 |       15 |       62 |       92 |
| collect.testing.testers  | ListGetTester.java                          | src      |        5 |        6 |       16 |       24 |       51 |
| collect.testing.testers  | ListHashCodeTester.java                     | src      |        4 |        9 |       15 |       24 |       52 |
| collect.testing.testers  | ListIndexOfTester.java                      | src      |        6 |        6 |       15 |       29 |       56 |
| collect.testing.testers  | ListLastIndexOfTester.java                  | src      |        6 |        6 |       15 |       31 |       58 |
| collect.testing.testers  | ListListIteratorTester.java                 | src      |       12 |       17 |       20 |       78 |      127 |
| collect.testing.testers  | ListRemoveAllTester.java                    | src      |        6 |        6 |       16 |       28 |       56 |
| collect.testing.testers  | ListRemoveAtIndexTester.java                | src      |       11 |        6 |       16 |       82 |      115 |
| collect.testing.testers  | ListRemoveTester.java                       | src      |        5 |        6 |       15 |       33 |       59 |
| collect.testing.testers  | ListReplaceAllTester.java                   | src      |        6 |        6 |       15 |       39 |       66 |
| collect.testing.testers  | ListRetainAllTester.java                    | src      |        6 |        6 |       15 |       47 |       74 |
| collect.testing.testers  | ListSetTester.java                          | src      |       15 |       15 |       15 |      104 |      149 |
| collect.testing.testers  | ListSubListTester.java                      | src      |       32 |       26 |       27 |      269 |      354 |
| collect.testing.testers  | ListToArrayTester.java                      | src      |        8 |        6 |       16 |       26 |       56 |
| collect.testing.testers  | MapClearTester.java                         | src      |        9 |        7 |       18 |       78 |      112 |
| collect.testing.testers  | MapComputeIfAbsentTester.java               | src      |       15 |        6 |       16 |      168 |      205 |
| collect.testing.testers  | MapComputeIfPresentTester.java              | src      |       15 |        6 |       15 |      146 |      182 |
| collect.testing.testers  | MapComputeTester.java                       | src      |       13 |        6 |       17 |      168 |      204 |
| collect.testing.testers  | MapContainsKeyTester.java                   | src      |       10 |        6 |       16 |       49 |       81 |
| collect.testing.testers  | MapContainsValueTester.java                 | src      |       10 |        7 |       16 |       50 |       83 |
| collect.testing.testers  | MapCreationTester.java                      | src      |       17 |       13 |       15 |      109 |      154 |
| collect.testing.testers  | MapEntrySetTester.java                      | src      |       19 |        7 |       17 |      125 |      168 |
| collect.testing.testers  | MapEqualsTester.java                        | src      |       17 |        6 |       15 |       93 |      131 |
| collect.testing.testers  | MapForEachTester.java                       | src      |        7 |        6 |       15 |       51 |       79 |
| collect.testing.testers  | MapGetOrDefaultTester.java                  | src      |       13 |        6 |       15 |       91 |      125 |
| collect.testing.testers  | MapGetTester.java                           | src      |       10 |        7 |       15 |       50 |       82 |
| collect.testing.testers  | MapHashCodeTester.java                      | src      |       11 |        6 |       15 |       54 |       86 |
| collect.testing.testers  | MapIsEmptyTester.java                       | src      |        5 |        6 |       15 |       18 |       44 |
| collect.testing.testers  | MapMergeTester.java                         | src      |       14 |       10 |       15 |      157 |      196 |
| collect.testing.testers  | MapPutAllTester.java                        | src      |       21 |       12 |       16 |      154 |      203 |
| collect.testing.testers  | MapPutIfAbsentTester.java                   | src      |       12 |        6 |       15 |       94 |      127 |
| collect.testing.testers  | MapPutTester.java                           | src      |       27 |       13 |       18 |      204 |      262 |
| collect.testing.testers  | MapRemoveEntryTester.java                   | src      |       11 |        6 |       18 |       70 |      105 |
| collect.testing.testers  | MapRemoveTester.java                        | src      |       15 |        7 |       18 |      125 |      165 |
| collect.testing.testers  | MapReplaceAllTester.java                    | src      |       10 |        6 |       15 |       95 |      126 |
| collect.testing.testers  | MapReplaceEntryTester.java                  | src      |       16 |        6 |       20 |      109 |      151 |
| collect.testing.testers  | MapReplaceTester.java                       | src      |       12 |        6 |       18 |       74 |      110 |
| collect.testing.testers  | MapSerializationTester.java                 | src      |        4 |        5 |       15 |       18 |       42 |
| collect.testing.testers  | MapSizeTester.java                          | src      |        3 |        6 |       15 |       11 |       35 |
| collect.testing.testers  | MapToStringTester.java                      | src      |       10 |        7 |       13 |       59 |       89 |
| collect.testing.testers  | NavigableMapNavigationTester.java           | src      |       33 |        7 |       16 |      209 |      265 |
| collect.testing.testers  | NavigableSetNavigationTester.java           | src      |       30 |       12 |       31 |      185 |      258 |
| collect.testing.testers  | Platform.java                               | src      |        7 |        5 |       15 |       19 |       46 |
| collect.testing.testers  | QueueElementTester.java                     | src      |        6 |        6 |       15 |       34 |       61 |
| collect.testing.testers  | QueueOfferTester.java                       | src      |        6 |        6 |       15 |       31 |       58 |
| collect.testing.testers  | QueuePeekTester.java                        | src      |        6 |        6 |       15 |       29 |       56 |
| collect.testing.testers  | QueuePollTester.java                        | src      |        6 |        6 |       15 |       33 |       60 |
| collect.testing.testers  | QueueRemoveTester.java                      | src      |        6 |        6 |       15 |       38 |       65 |
| collect.testing.testers  | SetAddAllTester.java                        | src      |        6 |        6 |       15 |       32 |       59 |
| collect.testing.testers  | SetAddTester.java                           | src      |        6 |       10 |       15 |       33 |       64 |
| collect.testing.testers  | SetCreationTester.java                      | src      |        9 |        7 |       15 |       55 |       86 |
| collect.testing.testers  | SetEqualsTester.java                        | src      |       13 |        5 |       15 |       63 |       96 |
| collect.testing.testers  | SetHashCodeTester.java                      | src      |        7 |       10 |       15 |       47 |       79 |
| collect.testing.testers  | SetRemoveTester.java                        | src      |        4 |        6 |       15 |       19 |       44 |
| collect.testing.testers  | SortedMapNavigationTester.java              | src      |       20 |        7 |       16 |      147 |      190 |
| collect.testing.testers  | SortedSetNavigationTester.java              | src      |       13 |        7 |       16 |       70 |      106 |
+--------------------------+---------------------------------------------+----------+----------+----------+----------+----------+----------+
| collect.testing          | AbstractCollectionTestSuiteBuilder.java     | src      |        3 |        5 |       16 |       55 |       79 |
| collect.testing          | AbstractCollectionTester.java               | src      |       10 |       12 |       18 |       39 |       79 |
| collect.testing          | AbstractContainerTester.java                | src      |       32 |       73 |       29 |      124 |      258 |
| collect.testing          | AbstractIteratorTester.java                 | src      |       64 |       78 |       54 |      413 |      609 |
| collect.testing          | AbstractMapTester.java                      | src      |       39 |       27 |       19 |      170 |      255 |
| collect.testing          | AbstractTester.java                         | src      |       10 |       11 |       19 |       42 |       82 |
| collect.testing          | AnEnum.java                                 | src      |        3 |        5 |       15 |       11 |       34 |
| collect.testing          | BaseComparable.java                         | src      |        8 |        5 |       15 |       29 |       57 |
| collect.testing          | CollectionTestSuiteBuilder.java             | src      |       13 |        8 |       15 |       64 |      100 |
| collect.testing          | ConcurrentMapTestSuiteBuilder.java          | src      |        5 |        6 |       13 |       28 |       52 |
| collect.testing          | ConcurrentNavigableMapTestSuiteBuilder.java | src      |        6 |        6 |       15 |       24 |       51 |
| collect.testing          | DerivedCollectionGenerators.java            | src      |       88 |        9 |       41 |      420 |      558 |
| collect.testing          | DerivedComparable.java                      | src      |        4 |        5 |       15 |        9 |       33 |
| collect.testing          | DerivedGenerator.java                       | src      |        3 |       11 |       15 |        6 |       35 |
| collect.testing          | DerivedTestIteratorGenerator.java           | src      |        6 |        5 |       15 |       20 |       46 |
| collect.testing          | FeatureSpecificTestSuiteBuilder.java        | src      |       43 |       23 |       28 |      218 |      312 |
| collect.testing          | Helpers.java                                | src      |       68 |       46 |       36 |      376 |      526 |
| collect.testing          | IteratorFeature.java                        | src      |        5 |       23 |       15 |       17 |       60 |
| collect.testing          | IteratorTester.java                         | src      |        4 |       39 |       15 |       18 |       76 |
| collect.testing          | ListIteratorTester.java                     | src      |        5 |       13 |       15 |       25 |       58 |
| collect.testing          | ListTestSuiteBuilder.java                   | src      |       16 |       10 |       15 |      116 |      157 |
| collect.testing          | MapInterfaceTest.java                       | src      |      137 |       58 |       58 |     1367 |     1620 |
| collect.testing          | MapTestSuiteBuilder.java                    | src      |       31 |        5 |       22 |      223 |      281 |
| collect.testing          | MinimalCollection.java                      | src      |       19 |        6 |       25 |       84 |      134 |
| collect.testing          | MinimalIterable.java                        | src      |        7 |       29 |       18 |       31 |       85 |
| collect.testing          | MinimalSet.java                             | src      |       10 |        7 |       18 |       47 |       82 |
| collect.testing          | NavigableMapTestSuiteBuilder.java           | src      |       25 |        4 |       16 |      138 |      183 |
| collect.testing          | NavigableSetTestSuiteBuilder.java           | src      |       17 |        4 |       16 |      116 |      153 |
| collect.testing          | OneSizeGenerator.java                       | src      |       12 |        5 |       15 |       53 |       85 |
| collect.testing          | OneSizeTestContainerGenerator.java          | src      |        5 |       10 |       15 |       11 |       41 |
| collect.testing          | PerCollectionSizeTestSuiteBuilder.java      | src      |       17 |       13 |       17 |       87 |      134 |
| collect.testing          | Platform.java                               | src      |        6 |        7 |       16 |       16 |       45 |
| collect.testing          | QueueTestSuiteBuilder.java                  | src      |        7 |       11 |       15 |       34 |       67 |
| collect.testing          | ReserializingTestCollectionGenerator.java   | src      |       10 |        7 |       15 |       49 |       81 |
| collect.testing          | ReserializingTestSetGenerator.java          | src      |        6 |        7 |       15 |       17 |       45 |
| collect.testing          | SafeTreeMap.java                            | src      |       55 |        6 |       16 |      250 |      327 |
| collect.testing          | SafeTreeSet.java                            | src      |       44 |        6 |       16 |      189 |      255 |
| collect.testing          | SampleElements.java                         | src      |       23 |        5 |       21 |      106 |      155 |
| collect.testing          | SetTestSuiteBuilder.java                    | src      |       15 |        5 |       18 |       83 |      121 |
| collect.testing          | SortedMapInterfaceTest.java                 | src      |       10 |        6 |       16 |       96 |      128 |
| collect.testing          | SortedMapTestSuiteBuilder.java              | src      |       16 |       14 |       15 |       87 |      132 |
| collect.testing          | SortedSetTestSuiteBuilder.java              | src      |       12 |       10 |       15 |       69 |      106 |
| collect.testing          | SpliteratorTester.java                      | src      |       14 |        8 |       17 |      154 |      193 |
| collect.testing          | TestCharacterListGenerator.java             | src      |        7 |       10 |       15 |       30 |       62 |
| collect.testing          | TestCollectionGenerator.java                | src      |        3 |        5 |       15 |        5 |       28 |
| collect.testing          | TestCollidingSetGenerator.java              | src      |        5 |        5 |       15 |       20 |       45 |
| collect.testing          | TestContainerGenerator.java                 | src      |        6 |       26 |       15 |       13 |       60 |
| collect.testing          | TestEnumMapGenerator.java                   | src      |       11 |        5 |       15 |       49 |       80 |
| collect.testing          | TestEnumSetGenerator.java                   | src      |        7 |        5 |       15 |       33 |       60 |
| collect.testing          | TestIntegerSetGenerator.java                | src      |        7 |       16 |       15 |       30 |       68 |
| collect.testing          | TestIntegerSortedSetGenerator.java          | src      |        4 |        6 |       15 |       16 |       41 |
| collect.testing          | TestIteratorGenerator.java                  | src      |        3 |        6 |       15 |        7 |       31 |
| collect.testing          | TestListGenerator.java                      | src      |        3 |        5 |       15 |        8 |       31 |
| collect.testing          | TestMapEntrySetGenerator.java               | src      |        9 |        5 |       15 |       36 |       65 |
| collect.testing          | TestMapGenerator.java                       | src      |        4 |        5 |       15 |        8 |       32 |
| collect.testing          | TestQueueGenerator.java                     | src      |        3 |        5 |       15 |        8 |       31 |
| collect.testing          | TestSetGenerator.java                       | src      |        3 |        5 |       15 |        8 |       31 |
| collect.testing          | TestSortedMapGenerator.java                 | src      |        7 |       21 |       15 |       13 |       56 |
| collect.testing          | TestSortedSetGenerator.java                 | src      |        7 |       21 |       15 |       12 |       55 |
| collect.testing          | TestStringCollectionGenerator.java          | src      |        7 |        5 |       15 |       31 |       58 |
| collect.testing          | TestStringListGenerator.java                | src      |        7 |        9 |       15 |       30 |       61 |
| collect.testing          | TestStringMapGenerator.java                 | src      |       10 |        7 |       15 |       48 |       80 |
| collect.testing          | TestStringQueueGenerator.java               | src      |        7 |        5 |       15 |       31 |       58 |
| collect.testing          | TestStringSetGenerator.java                 | src      |        7 |       16 |       15 |       30 |       68 |
| collect.testing          | TestStringSortedMapGenerator.java           | src      |       10 |        5 |       15 |       36 |       66 |
| collect.testing          | TestStringSortedSetGenerator.java           | src      |       10 |        5 |       15 |       37 |       67 |
| collect.testing          | TestSubjectGenerator.java                   | src      |        3 |        7 |       15 |        6 |       31 |
| collect.testing          | TestUnhashableCollectionGenerator.java      | src      |        7 |        9 |       15 |       31 |       62 |
| collect.testing          | TestsForListsInJavaUtil.java                | src      |       29 |        6 |       16 |      292 |      343 |
| collect.testing          | TestsForMapsInJavaUtil.java                 | src      |       50 |       10 |       22 |      517 |      599 |
| collect.testing          | TestsForQueuesInJavaUtil.java               | src      |       25 |        6 |       20 |      216 |      267 |
| collect.testing          | TestsForSetsInJavaUtil.java                 | src      |       49 |       10 |       15 |      498 |      572 |
| collect.testing          | UnhashableObject.java                       | src      |        8 |        5 |       16 |       29 |       58 |
| collect.testing          | WrongType.java                              | src      |        3 |        4 |       15 |        6 |       28 |
+--------------------------+---------------------------------------------+----------+----------+----------+----------+----------+----------+
| escape.testing           | EscaperAsserts.java                         | src      |       13 |       45 |       18 |       45 |      121 |
| escape.testing           | package-info.java                           | src      |        2 |        6 |       15 |        3 |       26 |
+--------------------------+---------------------------------------------+----------+----------+----------+----------+----------+----------+
| testing                  | AbstractPackageSanityTests.java             | src      |       26 |      159 |       22 |      236 |      443 |
| testing                  | ArbitraryInstances.java                     | src      |       35 |       35 |       25 |      426 |      521 |
| testing                  | ClassSanityTester.java                      | src      |       48 |      203 |       30 |      555 |      836 |
| testing                  | ClusterException.java                       | src      |        7 |       55 |       15 |       37 |      114 |
| testing                  | CollectorTester.java                        | src      |       14 |       31 |       15 |      108 |      168 |
| testing                  | DummyProxy.java                             | src      |       14 |        9 |       18 |       73 |      114 |
| testing                  | EqualsTester.java                           | src      |       11 |       54 |       15 |       60 |      140 |
| testing                  | EquivalenceTester.java                      | src      |       11 |       25 |       19 |       55 |      110 |
| testing                  | FakeTicker.java                             | src      |        9 |       17 |       15 |       34 |       75 |
| testing                  | ForwardingWrapperTester.java                | src      |       19 |       26 |       23 |      171 |      239 |
| testing                  | FreshValueGenerator.java                    | src      |      150 |       34 |       31 |      823 |     1038 |
| testing                  | GcFinalization.java                         | src      |       15 |      148 |       27 |      126 |      316 |
| testing                  | NullPointerTester.java                      | src      |       54 |      101 |       21 |      384 |      560 |
| testing                  | Platform.java                               | src      |        5 |        5 |       15 |       26 |       51 |
| testing                  | RelationshipTester.java                     | src      |       20 |       13 |       18 |      118 |      169 |
| testing                  | SerializableTester.java                     | src      |        5 |       54 |       15 |       20 |       94 |
| testing                  | SloppyTearDown.java                         | src      |        5 |        9 |       15 |       19 |       48 |
| testing                  | TearDown.java                               | src      |        3 |       22 |       15 |        9 |       49 |
| testing                  | TearDownAccepter.java                       | src      |        3 |       13 |       15 |        8 |       39 |
| testing                  | TearDownStack.java                          | src      |       10 |        8 |       15 |       54 |       87 |
| testing                  | TestLogHandler.java                         | src      |        8 |       27 |       22 |       32 |       89 |
| testing                  | package-info.java                           | src      |        1 |        4 |       15 |        2 |       22 |
+--------------------------+---------------------------------------------+----------+----------+----------+----------+----------+----------+
| util.concurrent.testing  | AbstractCheckedFutureTest.java              | src      |       29 |       11 |       17 |       95 |      152 |
| util.concurrent.testing  | AbstractListenableFutureTest.java           | src      |       45 |       17 |       27 |      158 |      247 |
| util.concurrent.testing  | MockFutureListener.java                     | src      |       12 |       18 |       19 |       44 |       93 |
| util.concurrent.testing  | SameThreadScheduledExecutorService.java     | src      |       27 |        8 |       15 |      139 |      189 |
| util.concurrent.testing  | TestingExecutors.java                       | src      |       21 |       48 |       15 |       86 |      170 |
+--------------------------+---------------------------------------------+----------+----------+----------+----------+----------+----------+
| 7 package(s)             | 288 file(s)                                 | java     |     4284 |     3615 |     4906 |    26355 |    39160 |
+--------------------------+---------------------------------------------+----------+----------+----------+----------+----------+----------+

[INFO] 
[INFO] --------------------< com.google.guava:guava-tests >--------------------
[INFO] Building Guava Unit Tests HEAD-jre-SNAPSHOT                        [4/5]
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- sloc-maven-plugin:0.1.3:sloc (default-cli) @ guava-tests ---
[WARNING] Does not contain a source directory: /home/orhanku/ME/DEV/x/guava/guava-tests/src
[INFO] 
[INFO] ---------------------< com.google.guava:guava-gwt >---------------------
[INFO] Building Guava GWT compatible libs HEAD-jre-SNAPSHOT               [5/5]
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- sloc-maven-plugin:0.1.3:sloc (default-cli) @ guava-gwt ---
[INFO] SLOC - directory: /home/orhanku/ME/DEV/x/guava/guava-gwt/src
+-------------------+-------------------------------------------------------+----------+----------+----------+----------+----------+----------+
| Package Name      | File Name                                             | Type     | Blank    | JavaDoc  | Comment  | Code     | Total    |
+-------------------+-------------------------------------------------------+----------+----------+----------+----------+----------+----------+
| common.base       | Absent_CustomFieldSerializer.java                     | src      |        5 |        9 |       15 |       12 |       41 |
| common.base       | GwtSerializationDependencies.java                     | src      |       16 |        9 |       15 |       58 |       98 |
| common.base       | PairwiseEquivalence_CustomFieldSerializer.java        | src      |        8 |        5 |       15 |       20 |       48 |
| common.base       | Present_CustomFieldSerializer.java                    | src      |        5 |        5 |       15 |       17 |       42 |
+-------------------+-------------------------------------------------------+----------+----------+----------+----------+----------+----------+
| common.collect    | AllEqualOrdering_CustomFieldSerializer.java           | src      |        5 |        5 |       15 |       10 |       35 |
| common.collect    | ArrayListMultimap_CustomFieldSerializer.java          | src      |        6 |        5 |       15 |       16 |       42 |
| common.collect    | ByFunctionOrdering_CustomFieldSerializer.java         | src      |        6 |        5 |       15 |       20 |       46 |
| common.collect    | ComparatorOrdering_CustomFieldSerializer.java         | src      |        6 |        5 |       15 |       18 |       44 |
| common.collect    | CompoundOrdering_CustomFieldSerializer.java           | src      |        6 |        5 |       15 |       27 |       53 |
| common.collect    | DenseImmutableTable_CustomFieldSerializer.java        | src      |        5 |        5 |       13 |       18 |       41 |
| common.collect    | EmptyImmutableListMultimap_CustomFieldSerializer.java | src      |        6 |        5 |       15 |       12 |       38 |
| common.collect    | EmptyImmutableSetMultimap_CustomFieldSerializer.java  | src      |        6 |        5 |       15 |       12 |       38 |
| common.collect    | ExplicitOrdering_CustomFieldSerializer.java           | src      |        6 |        5 |       15 |       16 |       42 |
| common.collect    | ForwardingImmutableList_CustomFieldSerializer.java    | src      |        2 |        6 |       15 |        2 |       25 |
| common.collect    | ForwardingImmutableSet_CustomFieldSerializer.java     | src      |        2 |        6 |       15 |        2 |       25 |
| common.collect    | GwtSerializationDependencies.java                     | src      |       19 |       27 |       28 |       60 |      134 |
| common.collect    | HashBasedTable_CustomFieldSerializer.java             | src      |        5 |        5 |       13 |       15 |       38 |
| common.collect    | HashMultimap_CustomFieldSerializer.java               | src      |        6 |        5 |       15 |       16 |       42 |
| common.collect    | HashMultiset_CustomFieldSerializer.java               | src      |        6 |        5 |       15 |       16 |       42 |
| common.collect    | ImmutableAsList_CustomFieldSerializer.java            | src      |        2 |        6 |       15 |        2 |       25 |
| common.collect    | ImmutableBiMap_CustomFieldSerializer.java             | src      |        2 |        6 |       15 |        2 |       25 |
| common.collect    | ImmutableEntry_CustomFieldSerializer.java             | src      |        6 |        5 |       15 |       18 |       44 |
| common.collect    | ImmutableEnumMap_CustomFieldSerializer.java           | src      |        6 |        5 |       20 |       20 |       51 |
| common.collect    | ImmutableEnumSet_CustomFieldSerializer.java           | src      |        6 |        5 |       20 |       19 |       50 |
| common.collect    | ImmutableListMultimap_CustomFieldSerializer.java      | src      |        6 |        5 |       15 |       18 |       44 |
| common.collect    | ImmutableList_CustomFieldSerializer.java              | src      |        2 |        6 |       15 |        2 |       25 |
| common.collect    | ImmutableMultiset_CustomFieldSerializer.java          | src      |        2 |        6 |       15 |        2 |       25 |
| common.collect    | ImmutableSetMultimap_CustomFieldSerializer.java       | src      |        6 |        5 |       16 |       26 |       53 |
| common.collect    | ImmutableSet_CustomFieldSerializer.java               | src      |        2 |        6 |       15 |        2 |       25 |
| common.collect    | ImmutableSortedMap_CustomFieldSerializer.java         | src      |        5 |        7 |       15 |       16 |       43 |
| common.collect    | ImmutableSortedMap_CustomFieldSerializerBase.java     | src      |        8 |        6 |       19 |       24 |       57 |
| common.collect    | ImmutableSortedSet_CustomFieldSerializer.java         | src      |        2 |        6 |       15 |        2 |       25 |
| common.collect    | ImmutableTable_CustomFieldSerializerBase.java         | src      |        5 |        6 |       13 |       30 |       54 |
| common.collect    | IndexedImmutableSet_CustomFieldSerializer.java        | src      |        2 |        7 |       15 |        2 |       26 |
| common.collect    | JdkBackedImmutableBiMap_CustomFieldSerializer.java    | src      |        6 |        5 |       15 |       20 |       46 |
| common.collect    | JdkBackedImmutableMap_CustomFieldSerializer.java      | src      |        6 |        5 |       15 |       20 |       46 |
| common.collect    | JdkBackedImmutableMultiset_CustomFieldSerializer.java | src      |        6 |        5 |       15 |       16 |       42 |
| common.collect    | JdkBackedImmutableSet_CustomFieldSerializer.java      | src      |        6 |        5 |       15 |       19 |       45 |
| common.collect    | LexicographicalOrdering_CustomFieldSerializer.java    | src      |        6 |        5 |       15 |       18 |       44 |
| common.collect    | LinkedHashMultimap_CustomFieldSerializer.java         | src      |        8 |        5 |       15 |       41 |       69 |
| common.collect    | LinkedHashMultiset_CustomFieldSerializer.java         | src      |        6 |        5 |       15 |       17 |       43 |
| common.collect    | LinkedListMultimap_CustomFieldSerializer.java         | src      |        6 |        5 |       15 |       27 |       53 |
| common.collect    | Multimap_CustomFieldSerializerBase.java               | src      |        7 |        8 |       15 |       50 |       80 |
| common.collect    | Multiset_CustomFieldSerializerBase.java               | src      |        6 |        7 |       15 |       26 |       54 |
| common.collect    | NaturalOrdering_CustomFieldSerializer.java            | src      |        6 |        5 |       15 |       10 |       36 |
| common.collect    | NullsFirstOrdering_CustomFieldSerializer.java         | src      |        6 |        5 |       15 |       17 |       43 |
| common.collect    | NullsLastOrdering_CustomFieldSerializer.java          | src      |        6 |        5 |       15 |       16 |       42 |
| common.collect    | Range_CustomFieldSerializer.java                      | src      |       13 |        5 |       15 |       46 |       79 |
| common.collect    | RegularImmutableAsList_CustomFieldSerializer.java     | src      |        6 |        5 |       15 |       23 |       49 |
| common.collect    | RegularImmutableBiMap_CustomFieldSerializer.java      | src      |        5 |        5 |       15 |       20 |       45 |
| common.collect    | RegularImmutableList_CustomFieldSerializer.java       | src      |        6 |        5 |       21 |       21 |       53 |
| common.collect    | RegularImmutableMap_CustomFieldSerializer.java        | src      |        6 |        5 |       15 |       20 |       46 |
| common.collect    | RegularImmutableMultiset_CustomFieldSerializer.java   | src      |        5 |        5 |       15 |       16 |       41 |
| common.collect    | RegularImmutableSet_CustomFieldSerializer.java        | src      |        6 |        5 |       15 |       19 |       45 |
| common.collect    | RegularImmutableSortedSet_CustomFieldSerializer.java  | src      |        7 |        5 |       25 |       26 |       63 |
| common.collect    | ReverseNaturalOrdering_CustomFieldSerializer.java     | src      |        6 |        5 |       15 |       11 |       37 |
| common.collect    | ReverseOrdering_CustomFieldSerializer.java            | src      |        6 |        5 |       15 |       16 |       42 |
| common.collect    | SingletonImmutableBiMap_CustomFieldSerializer.java    | src      |        7 |        5 |       15 |       21 |       48 |
| common.collect    | SingletonImmutableList_CustomFieldSerializer.java     | src      |        6 |        5 |       15 |       17 |       43 |
| common.collect    | SingletonImmutableSet_CustomFieldSerializer.java      | src      |        6 |        5 |       15 |       17 |       43 |
| common.collect    | SingletonImmutableTable_CustomFieldSerializer.java    | src      |        5 |        5 |       13 |       22 |       45 |
| common.collect    | SparseImmutableTable_CustomFieldSerializer.java       | src      |        5 |        5 |       13 |       18 |       41 |
| common.collect    | Table_CustomFieldSerializerBase.java                  | src      |        5 |        8 |       18 |       21 |       52 |
| common.collect    | TreeBasedTable_CustomFieldSerializer.java             | src      |        6 |        5 |       13 |       24 |       48 |
| common.collect    | TreeMultimap_CustomFieldSerializer.java               | src      |        7 |        5 |       15 |       23 |       50 |
| common.collect    | UsingToStringOrdering_CustomFieldSerializer.java      | src      |        6 |        5 |       15 |       11 |       37 |
+-------------------+-------------------------------------------------------+----------+----------+----------+----------+----------+----------+
| common.primitives | UnsignedLong_CustomFieldSerializer.java               | src      |        5 |        5 |       13 |       15 |       38 |
+-------------------+-------------------------------------------------------+----------+----------+----------+----------+----------+----------+
| common            | ForceGuavaCompilationEntryPoint.java                  | src      |        3 |        5 |       15 |        6 |       29 |
+-------------------+-------------------------------------------------------+----------+----------+----------+----------+----------+----------+
| 4 package(s)      | 68 file(s)                                            | java     |      396 |      392 |     1053 |     1262 |     3103 |
+-------------------+-------------------------------------------------------+----------+----------+----------+----------+----------+----------+

[INFO] ------------------------------------------------------------------------
[INFO] Reactor Summary:
[INFO] 
[INFO] Guava Maven Parent HEAD-jre-SNAPSHOT ............... SUCCESS [  0.207 s]
[INFO] Guava: Google Core Libraries for Java .............. SUCCESS [  0.565 s]
[INFO] Guava Testing Library .............................. SUCCESS [  0.171 s]
[INFO] Guava Unit Tests ................................... SUCCESS [  0.080 s]
[INFO] Guava GWT compatible libs HEAD-jre-SNAPSHOT ........ SUCCESS [  0.253 s]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 1.849 s
[INFO] Finished at: 2018-11-15T10:33:54+03:00
[INFO] ------------------------------------------------------------------------
```
