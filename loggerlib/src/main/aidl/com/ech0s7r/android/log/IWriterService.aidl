package com.ech0s7r.android.log;

interface IWriterService {

    /**
     * Add a message to the log message queue
     */
	void addInQueue(String msg);

    /**
     * Stop the writer service
     */
	void stop();

}