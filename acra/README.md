[![Click here to lend your support to: ACRA - Application Crash Reports for Android and make a donation at www.pledgie.com !](https://pledgie.com/campaigns/18789.png?skin_name=chrome)](http://www.pledgie.com/campaigns/18789) [![Flattr this project](http://api.flattr.com/button/flattr-badge-large.png)](https://flattr.com/submit/auto?user_id=kevingaudin&url=http://acra.ch&title=ACRA%20-%20Application%20Crash%20Reports%20for%20Android&language=&tags=opensource%2Candroid&category=software&description=ACRA%20%28Application%20Crash%20Reports%20for%20Android%29%20is%20an%20open%20source%20android%20library%20for%20developers%2C%20enabling%20their%20apps%20to%20send%20detailed%20reports%20when%20they%20crash.)

<a href="https://plus.google.com/118444843928759726538" rel="publisher">Follow ACRA on Google+ for latest news and tips.</a>

[![](https://ssl.gstatic.com/images/icons/gplus-32.png)](https://plus.google.com/118444843928759726538)

What is ACRA ?
==============

ACRA is a library enabling Android Application to automatically post their crash reports to a GoogleDoc form. It is targetted to android applications developers to help them get data from their applications when they crash or behave erroneously.

ACRA is used in 2.76% ([See AppBrain/stats](http://www.appbrain.com/stats/libraries/details/acra/acra)) of all apps on Google Play as of Jan 2015. That's over 40M **apps** using ACRA. And since the average US user has 41 apps installed on their phone that means there is a 70% chance that ACRA is running on any phone. That means ACRA is running on over a **billion devices**.

See [BasicSetup](http://github.com/ACRA/acra/wiki/BasicSetup) for a step-by-step installation and usage guide.

A crash reporting feature for android apps is native since Android 2.2 (FroYo) but only available through the official Android Market (and with limited data). ACRA is a great help for Android developers :

  * [developer configurable user interaction](http://github.com/ACRA/acra/wiki/AdvancedUsage#wiki-User_Interaction): silent reports, Toast notification, status bar notification + dialog or direct dialog
  * usable with ALL versions of android (compiled with 1.5, not tested on 1.0/1.1 but might work... but who does really care ?) and capable of retrieving data from latest versions through reflection.
  * more [detailed crash reports](http://github.com/ACRA/acra/wiki/ReportContent) about the device running the app than what is displayed in the Android Market developer console error reports
  * you can [add your own variables content or debug traces](http://github.com/ACRA/acra/wiki/AdvancedUsage#wiki-Adding_your_own_variables_content_or_traces_in_crash_reports) to the reports
  * you can send [error reports even if the application doesn't crash](http://github.com/ACRA/acra/wiki/AdvancedUsage#wiki-Sending_reports_for_caught_exceptions)
  * works for any application even if not delivered through Google's Android Market => great for devices/regions where the Android Market is not available, beta releases or for enterprise private apps
  * if there is no network coverage, reports are kept and sent on a later application restart
  * can be used with [your own self-hosted report receiver script](http://github.com/ACRA/acra/wiki/AdvancedUsage#wiki-Reports_destination)
  * google doc reports can be shared with a whole development team. Other benefits from the Google Docs platform are still to be investigated (stats, macros...)

ACRA's notification systems are clean. If a crash occurs, your application does not add user notifications over existing system's crash notifications or reporting features. If you use the Toast, Status bar notification or direct dialog modes, the "force close" dialog is not displayed anymore and devices where the system native reporting feature is enabled do not offer the user to send an additional report.

The user is notified of an error only once, and you might enhance the percieved quality of your application by defining your own texts in the notifications/dialogs.

Please do not hesitate to open defects/enhancements requests in [the issue tracker](http://github.com/ACRA/acra/issues).

Change Log
==========

For a complete changelog, please see the [ChangeLog page](http://github.com/ACRA/acra/wiki/ChangeLog) in the Wiki.

ACRA v4.7
===============================
- Support for Android M (6.0)
  - Using HtttpUrlConnection instead of Apache Http
  - Using com.android.support:support-v4 to provide support fro removed Notification methods.
- Minimum Android version is Froyo (2.2). ACRA will disable itself for anything prior to that.  
- Packaging as an AAR.
  In order to use com.android.support:support-v4 ACRA now needs to be packaged as an AAR instead of a JAR.
- Removal of maxNumberOfRequestRetries config as HttpUrlConnection does not natively support retries on Socket timeout.
- Removal of httpsSocketFactoryFactoryClass config and TlsSniSocketFactory as it was not natively supported with HttpUrlConnection and it is questionable as to whether it is still required.
  HttpUrlConnection decides upon the Socket to use based upon protocol.
- Removal of disableSSLCertValidation (this hasn't been used since the introduction of the keystore parameter).  
- Increase of default connection timeout to 5000ms
- Increase of default socket timeout to 8000ms


ACRA v4.6
===============================

The summarized changelog is here: https://github.com/ACRA/acra/wiki/ChangeLog

Included in this release (summarized summary):
- CustomReportDialog using @ReportCrashes#reportDialogClass (NB must extend from BaseCrashReportDialog).
- many bugfixes

ACRA v4.5 - enabling the future
===============================

**ACRA 4.5.0 is now the official stable version.**

https://oss.sonatype.org/content/groups/public/ch/acra/acra/4.5.0/acra-4.5.0.zip
(also available in Maven Central repository)

The summarized changelog is here: https://github.com/ACRA/acra/wiki/ChangeLog

Included in this release (summarized summary):
- many bugfixes
- no more exception thrown in ACRA.init() if called twice (widget developers will enjoy it)
- HttpPostSender is renamed HttpSender and can send PUT and POST requests with data encoded as FORM (same as before) and JSON. The JSON mode enables a fully structured JSON tree to be sent to your backend.
- Display configuration details can benefit of the newly introduced DisplayManager from Android 4.2
- CrashReportDialog is now using AlertDialog.Builder to ensure that dialogs are created using the UX guidelines enforced by the android version. (you should remove its theme attribute in your manifest to benefit from the default theme of the device)
- Ability to set Http Headers with `ACRAConfig.setHttpHeaders()`

The most important part of this release is to enable the usage of Acralyzer (http://github.com/ACRA/acralyzer) which will be the default backend in future release.

Next release will be 5.0 with important changes in mind:
- no more default support of old Google Forms
- use JSON as the default report storage and management mode (current implementation transforms flat data into JSON just before sending it)

New ideas about the project are always welcome, you can open feature requests in the Github issue tracker.


ACRA v4.4 - enforcing security
==============================

ACRA has been named in [this report](http://www.cs.utexas.edu/~shmat/shmat_ccs12.pdf) as a potential cause of SSL vulnerability for all android apps using it.

The truth is that, in order to let devs use alternative backends over an SSL connection with self-signed certificates, I chose to disable certificate validation in earlier versions of the lib. But this was done only on the scope of ACRA reports senders. Using ACRA did not imply that your app became unsafe for all its SSL communications.

Prior to ACRA v4.4.0, reports content were indeed vulnerable to a man in the middle attack. There "can" be some private data in there, but there are really few by default.

ACRA v4.4.0 has been modified to use SSL certificate validation by default. If you send your reports to your own server via SSL with a self-signed certificate, you have to set the option `disableSSLCertValidation` to `true` (annotation or dynamic config).

ACRA v4.3 is now STABLE
=======================

After 15 months of great service and more than 11700 downloads, it's time for v4.2.3 to bow out and live a new life among the deprecated releases.

Here's what's new in ACRA 4.3.0:

* cleaned, more stable code base, reducing reports duplicates (thanks to William Ferguson)
* new experimental and long awaited **direct dialog** interaction mode, _without notifications_ (thanks to Julia Segal)
* full **runtime configuration API**, required for projects using Android Library Projects since ADT14, and very handy for developers in need of dynamic ACRA configuration.
* addition of a collector for a custom log file
* addition of a collector for the details of the broken thread (id, name, groupname)
* addition of a collector for the new MediaCodecList provided in the Jelly Bean API

A more detailed description of the changes has been introduced in [this Google+ post](https://plus.google.com/b/118444843928759726538/118444843928759726538/posts/cnABXX7bbxV), based on the [ChangeLog](acra/wiki/ChangeLog).

If you upgrade from 4.2.3, be aware that the default list of ReportFields has changed. You would better create a new spreadsheet & form with the help of the doc/CrashReports-Template.csv or use `@ReportsCrashes(customReportContent={...})` to redefine your own list of fields.

Thanks a lot to everyone for testing during these 3 weeks of Beta (with special thanks to Nikolay Elenkov for his feedback on the dynamic configuration API), the 3 successive beta releases have reached 397 downloads on googlecode, not including Maven downloads. There has been very few reports during the Beta, a proof that you can rely on this new version even more than you could rely on the previous.

About Maven. ACRA is now available on Maven Central, with 4.2.3 and 4.3.0 stable releases available on the central repository. Just note these IDs: groupId `ch.acra` artifactId `acra`.

If you think there are missing parts in the documentation, please open an issue. 

_Kevin_

----

ACRA v4.X main new features
===========================

You can read in the [ChangeLog](http://code.google.com/p/acra/acra/wiki/ChangeLog) that many things have been added since ACRA 3.1. Here is a summary:

  * In addition to standard logcat data, reports can contain eventslog and radioevents data
  * Reports will contain the result of the "`adb shell dumpsys meminfo <pid>`" command which gives details about your application memory usage right after the crash.
  * Introduction of an abstraction layer for report senders. This allows to:
    * use the `formUri` parameter to send reports to your custom server script with POST parameters names not related to Google Forms naming. POST parameters will have easy to understand names.
    * introduce a new report sending mode: email (see below)
    * create your own custom report senders. There is now a simple public interface allowing you to code your own class in charge of handling report data. Your sender(s) can be added to default senders or replace them.
  * Reports can now be sent via email (through an `ACTION_SEND` intent so the user has to choose the email client he wants to use and then send the email containing report fields in the body). The list of report fields included is configurable. This allows to get rid of the `INTERNET` permission in apps where it does not make any sense.
  * Custom report receiver server scripts can be secured with basic http authentication (login/password can be configured in ACRA)
  * If the `READ_PHONE_STATE` permission is granted, reports include the Unique Device Identifier (IMEI). This can be really useful for enterprise applications deployment.

-----

And after that?
===============

Now that ACRA is stabilized on the device side (there shouldn't be much more data required...), the effort should be placed on crash data analysis and reports management tools for developers.

You can look at [some contributions](http://github.com/ACRA/acra/wiki/Contribs) that have already been published. Most of them are work in progress, so if you feel like joining the effort, please do!

[Acralyzer](http://github.com/ACRA/acralyzer) will soon be the official backend for reports storage and analysis. It is a free and open source modern web app, based on a full open stack and using advanced
technology like CouchDB (JSON document storage with a RESTful API and Map/Reduce querying), AngularJS (one of the most advanced client-side JS frameworks), D3JS (for data visualisation)... If you are interested
in webapps development, this project can become your playground too ;-)
