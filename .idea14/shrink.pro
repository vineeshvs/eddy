-injars eddy.zip(!META-INF/MANIFEST.MF,!META-INF/LICENSE.txt,!META-INF/NOTICE.txt)
-outjars eddy.jar 
-dontobfuscate

# this causes problems for some people: LVTT entry ... does not match any LVT entry"
-optimizations !code/allocation/variable

### Keep a few things

-keep class com.eddysystems.eddy.EddyPlugin,com.eddysystems.eddy.EddyIntention,com.eddysystems.eddy.CorrectionIntention {
  <init>(...);
}

-keep class com.eddysystems.eddy.EddyEnterHandlerDelegate,com.eddysystems.eddy.Preferences {
  <init>(...);
}

-keep class com.eddysystems.eddy.actions.NextSuggestion,com.eddysystems.eddy.actions.PrevSuggestion,com.eddysystems.eddy.actions.Reinit {
  <init>(...);
}

### Libraries

# Base
-libraryjars /System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Classes/classes.jar
-libraryjars /System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Classes/jsse.jar
-libraryjars /System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Classes/jce.jar

# IntelliJ proper
-libraryjars "/Applications/IntelliJ IDEA 14 CE.app/Contents/lib/annotations.jar"
-libraryjars "/Applications/IntelliJ IDEA 14 CE.app/Contents/lib/extensions.jar"
-libraryjars "/Applications/IntelliJ IDEA 14 CE.app/Contents/lib/idea.jar"
-libraryjars "/Applications/IntelliJ IDEA 14 CE.app/Contents/lib/openapi.jar"
-libraryjars "/Applications/IntelliJ IDEA 14 CE.app/Contents/lib/util.jar"
#-libraryjars "/Applications/IntelliJ IDEA 14 CE.app/Contents/lib/forms_rt.jar"

# IntelliJ dependencies
-libraryjars "/Applications/IntelliJ IDEA 14 CE.app/Contents/lib/commons-logging-1.1.3.jar"
-libraryjars "/Applications/IntelliJ IDEA 14 CE.app/Contents/lib/log4j.jar"
-libraryjars "/Applications/IntelliJ IDEA 14 CE.app/Contents/lib/trove4j.jar"
-libraryjars "/Applications/IntelliJ IDEA 14 CE.app/Contents/lib/velocity.jar"

### Ignore various warnings and notes

# Scala warnings
#-dontwarn scala.**
#-dontnote scala.**
-dontnote String

# Miscellaneous warnings
-dontwarn javax.servlet.ServletContextListener
-dontwarn javax.servlet.ServletContextEvent
-dontwarn org.apache.log.Logger
-dontwarn org.apache.log.Hierarchy
-dontwarn org.apache.avalon.framework.logger.Logger
-dontwarn org.joda.convert.FromString
-dontwarn org.joda.convert.ToString
-dontwarn com.intellij.util.net.ssl.ConfirmingHostnameVerifier
-dontwarn com.intellij.uiDesigner.core.**

# Miscellaneous notes
-dontnote org.apache.commons.logging.**
-dontnote org.apache.http.client.utils.JdkIdn
-dontnote com.amazonaws.metrics.internal.cloudwatch.DefaultMetricCollectorFactory
-dontnote org.bouncycastle.jce.provider.BouncyCastleProvider
-dontnote org.joda.time.DateTimeZone
-dontnote com.eddysystems.eddy.engine.Formatter$VersionIncompatibilityCircumventer

# Weird scala issue
-dontwarn tarski.Semantics$$anonfun$denoteStmt$27$$anonfun$tarski$Semantics$$anonfun$$rest$1$1
