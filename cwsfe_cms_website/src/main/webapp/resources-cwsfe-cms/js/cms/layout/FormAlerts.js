define('formAlerts', ['jquery', 'knockout'], function ($, ko) {

    let FormAlerts = function () {
        const self = this;

        self.formMessages = ko.observableArray([]);

        self.errorMessages = ko.pureComputed(function () {
            return ko.utils.arrayFilter(self.formMessages(), function (formMessage) {
                return (formMessage.type === 'error');
            });
        }, self);
        self.infoMessages = ko.pureComputed(function () {
            return ko.utils.arrayFilter(self.formMessages(), function (formMessage) {
                return (formMessage.type === 'info');
            });
        }, self);
        self.warningMessages = ko.pureComputed(function () {
            return ko.utils.arrayFilter(self.formMessages(), function (formMessage) {
                return (formMessage.type === 'warning');
            });
        }, self);

        self.addMessage = function (msgText) {
            self.formMessages.push({
                msg: msgText,
                type: 'error'
            });
        };
        self.addError = function (msgText) {
            self.formMessages.push({
                msg: msgText,
                type: 'error'
            });
        };
        self.addWarning = function (msgText) {
            self.formMessages.push({
                msg: msgText,
                type: 'warning'
            });
        };
        self.addInfo = function (msgText) {
            self.formMessages.push({
                msg: msgText,
                type: 'info'
            });
        };
        self.cleanMessage = function (typeType) {
            const itemsForRemove = ko.utils.arrayFilter(self.formMessages(), function (formMessage) {
                return formMessage.type === typeType;
            });
            self.formMessages.removeAll(itemsForRemove);
        };
        self.cleanAllMessages = function () {
            self.formMessages.removeAll(self.formMessages());
        };

        self.showMessageAnimation = function (elem) {
            $(elem).hide().slideDown();
        };

        self.hideMessageAnimation = function (elem) {
            $(elem).slideUp('slow', function () {
                $(elem).remove();
            });
        };
    };

    return {formAlerts: FormAlerts};

});
