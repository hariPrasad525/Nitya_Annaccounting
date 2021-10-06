// For an introduction to the Blank template, see the following documentation:
// http://go.microsoft.com/fwlink/?LinkId=232509
(function () {
    "use strict";

    var app = WinJS.Application;
    var activation = Windows.ApplicationModel.Activation;
    WinJS.strictProcessing();
    

    app.onactivated = function (args) {
        if (args.detail.kind === activation.ActivationKind.launch) {
            if (args.detail.previousExecutionState !== activation.ApplicationExecutionState.terminated) {
                // TODO: This application has been newly launched. Initialize
                // your application here.
            } else {
                // TODO: This application has been reactivated from suspension.
                // Restore application state here.
            }
            args.setPromise(WinJS.UI.processAll());
        }
    };
    /*
    WinJS.Application.onsettings = function (e) {
    e.detail.applicationcommands = {
        "gameOptionsDiv": { title: "Game options", href: "/html/4-SettingsFlyout-Game.html" }
    };
    WinJS.UI.SettingsFlyout.populateSettings(e);
    };
    _settingsEvent.event = e.detail;

        if (_settingsEvent.event.applicationcommands) {
            var n = Windows.UI.ApplicationSettings;
            Object.keys(_settingsEvent.event.applicationcommands).forEach(function (name) {
                var setting = _settingsEvent.event.applicationcommands[name];
                if (!setting.title) { setting.title = name; }
                var command = new n.SettingsCommand(name, setting.title, thisWinUI.SettingsFlyout._onSettingsCommand);
                _settingsEvent.event.e.request.applicationCommands.append(command);
            });
        }*/
    function _onPrivacyPolicy(command) {
        var uri = new Windows.Foundation.Uri('http://www.annaccounting.com/content/privacypolicy');
        Windows.System.Launcher.launchUriAsync(uri);
    }
    function _onTermsAndConditions(command) {
        var uri = new Windows.Foundation.Uri('http://www.annaccounting.com/content/termsandconditions');
        Windows.System.Launcher.launchUriAsync(uri);
    }
    function _onSettings(command) {
        document.getElementById('settingsContainer').winControl.show();
    }
    WinJS.Application.onsettings = function (e) {
        var n = Windows.UI.ApplicationSettings;
        var privacy = new n.SettingsCommand('privacy', 'Privacy Policy', _onPrivacyPolicy);
        var terms = new n.SettingsCommand('terms', 'Terms & Conditions', _onTermsAndConditions);
       // var settings = new n.SettingsCommand('Settings', 'Settings', _onSettings);
       // e.detail.e.request.applicationCommands.append(settings);
        e.detail.e.request.applicationCommands.append(terms);
        e.detail.e.request.applicationCommands.append(privacy);
    };

    app.oncheckpoint = function (args) {
        // TODO: This application is about to be suspended. Save any state
        // that needs to persist across suspensions here. You might use the
        // WinJS.Application.sessionState object, which is automatically
        // saved and restored across suspension. If you need to complete an
        // asynchronous operation before your application is suspended, call
        // args.setPromise().
    };
    WinJS.Application.onerror = function (e) {

        // The first argument give us details of the exception:
        var message = e.detail.message;
        var description = e.detail.description;
        var code = e.detail.number;
        var stackTrace = e.detail.stack;

        // ...

        // By returning true, we signal that the exception was handled,
        // preventing the application from being terminated
        return true;
    };

    window.downloadFile= function(url,name) {

        // Verify that we are currently not snapped, or that we can unsnap to open the picker
        var currentState = Windows.UI.ViewManagement.ApplicationView.value;
        if (currentState === Windows.UI.ViewManagement.ApplicationViewState.snapped &&
            !Windows.UI.ViewManagement.ApplicationView.tryUnsnap()) {
            // Fail silently if we can't unsnap
            return;
        }

        // Create the picker object and set options
        var savePicker = new Windows.Storage.Pickers.FolderPicker();
        savePicker.fileTypeFilter.replaceAll(["*"]);
        savePicker.suggestedStartLocation = Windows.Storage.Pickers.PickerLocationId.documentsLibrary;

        savePicker.pickSingleFolderAsync().then(function (folder) {
            if (folder) {
                // write to file
                var uri = new Windows.Foundation.Uri(url);
                if (!name) {
                    name = uri.queryParsed.getFirstValueByName("filename");
                }
                var downloader = new Windows.Networking.BackgroundTransfer.BackgroundDownloader();;
                folder.createFileAsync(name,
                    Windows.Storage.CreationCollisionOption.ReplaceExisting).then(function (file) {;
                        downloader.createDownload(uri, file).startAsync().done(function () {
                            WinJS.log && WinJS.log("Operation cancelled.", "sample", "status");
                        });
                    });
            } else {
                WinJS.log && WinJS.log("Operation cancelled.", "sample", "status");
            }
        });
    };
    app.start();
})();
