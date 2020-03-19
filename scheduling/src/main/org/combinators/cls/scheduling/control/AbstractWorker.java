package org.combinators.cls.scheduling.control;

import org.combinators.cls.scheduling.view.MainWindowAUI;

/**
 Abstract class for all workers */
abstract class AbstractWorker extends Thread {
	/**
	 GUI callback field
	 */
	protected final MainWindowAUI callback;
	
	/**
	 Flag to be set to true when worker ist working
	 Set and false if the worker has finished or cancellation is scheduled
	 */
	protected volatile boolean running;
	
	/**
	 Creates a new Worker with given callback AUI
	 @param callback GUI callback AUI
	 */
	AbstractWorker(MainWindowAUI callback) {
		this.callback = callback;
		this.setDaemon(true);
	}
	
	/**
	 Override method inherited from java.lang.Thread
	 Sets running lag and invokes work method
	 */
	@Override
	public void run() {
		this.running = true; //Set running state
		work();
		callback.onFinishedOrCanceled();
		this.running = false; //Set finished
	}
	
	/**
	 Requests worker cancellation by setting running flag to false
	 */
	void cancel() {
		running = false;
	}
	
	/**
	 Work method to be implemented in concrete classes
	 */
	abstract void work();
}
