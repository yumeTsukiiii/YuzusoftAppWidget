// AlarmTaskInterface.aidl
package com.yumetsuki.yuzusoftappwidget;

// Declare any non-default types here with import statements

interface AlarmTaskInterface {

    void updateAlarm(int id);

    void newAlarm(int id);

    void removeAlarm(int id);

}
