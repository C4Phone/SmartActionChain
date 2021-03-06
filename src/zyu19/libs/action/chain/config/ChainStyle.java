package zyu19.libs.action.chain.config;

import zyu19.libs.action.chain.ReadOnlyChain;

/**
 * This interface restricts ActionChain to avoid callback hell, and to enable
 * the users of this library to choose a thread for every "action" (or 'PureAction'
 * object).
 * <p>
 * Although this looks like Promise, it does NOT follow the standard of Promise-Then.
 * <p>
 * Note: You could return a "new ActionChain. ... .start()" (which is called a subChain) inside a ".then()", which will force
 * the outermost ActionChain wait for the subChain. The result of the subChain will also be passed to the next ".then()" in
 * outermost ActionChain.
 * <p>
 * Created on 7/24/2015.
 * @author Zhongzhi Yu 
 * 
 * @version 0.4
 */
public interface ChainStyle <ThisType extends ChainStyle<ThisType>> {
	/**
	 *  Start running the 'PureAction' objects in ChainStyle.
	 *  <p>
	 *  Your actions will be copied to another object so that
	 *  you can call clear() immediately after start().
	 * @param onSuccess if not null, it will be called after all actions finish
	 * without Exception.
	 * @param <In> The input type of this action. Lambda will automatically set this template parameter.
	 * @return an Object representing the sequence of PureAction you created. Usually
     * this object is useless but if you return this object inside another PureAction,
     * then that PureAction will wait for this chain of actions to finish before it can finish.
	 */
	<In> ReadOnlyChain start(NiceConsumer<In> onSuccess);

	/**
	 *  Start running the 'PureAction' objects in ChainStyle, without a onSuccess listener --
	 *  useful for returning subChains inside another Chain's .then()
	 *  <p>
	 *  Your actions will be copied to another object so that
	 *  you can call clear() immediately after start().
	 * @param <In> The input type of this action. Lambda will automatically set this template parameter.
	 * @return an Object representing the sequence of PureAction you created. Usually
	 * this object is useless but if you return this object inside another PureAction,
	 * then that PureAction will wait for this chain of actions to finish before it can finish.
	 */
	<In> ReadOnlyChain start();

	/**
	 * Clear all actions. You can call this function after start() so as to arrange a
	 * new sequence of actions.
	 * @param onFailure the new onFailure callback for actions added afterwards. It
	 * can also be set to null.
	 * @return this object, thus enabling method chaining.
	 */
	ThisType clear(NiceConsumer<ErrorHolder> onFailure);

	/**
	 * set the onFailure callback for 'PureAction' objects added <b>subsequently</b>
	 * <br><b>Note: uncaught exceptions will be ignored so that it will not crash your app.</b>
	 * @param onFailure a callback to be invoked when any Exception is thrown from "actions"
	 * (or 'PureAction' objects).
	 * @return this object, thus enabling method chaining.
	 */
	ThisType fail(NiceConsumer<ErrorHolder> onFailure);

	/**
	 * set the onFailure callback for 'PureAction' objects added <b>subsequently</b>
	 * Adding additional fail <b>with class constraints</b> will not wipe out your previous default failure handler,
	 *   but the original handler will not be called if the specified exception was caught by this new onFailure handler.
	 * <br><b>Note: uncaught exceptions will be ignored so that it will not crash your app.</b>
	 * @param claz specifying the type of Exception you want to catch, other types will not be caught.
	 * @param onFailure a callback to be invoked when any Exception is thrown from "actions"
	 * (or 'PureAction' objects).
	 * @param <T> The type of Exception that this handler is looking for
	 * @return this object, thus enabling method chaining
	 */
	<T extends Exception> ThisType fail(Class<T> claz, NiceConsumer<ErrorHolder<T>> onFailure);

	/**
	 * Add a 'PureAction' object, or an "action", in this ChainStyle.
	 * @param runOnWorkerThread if set to false, the action will run on the main thread (UI thread)
	 * specified by ThreadChanger. Otherwise the task will run on any other thread (worker thread).
	 * @param action the action to be added.
	 * @param <In> The input type of this action. Lambda will automatically set this template parameter.
	 * @param <Out> The output type of this action. Lambda will automatically set this template parameter.
	 * @return this object, thus enabling method chaining.
	 */
	<In, Out> ThisType then(boolean runOnWorkerThread, PureAction<In, Out> action);

	<Out> ThisType then(boolean runOnWorkerThread, Producer<Out> action);


	/**
	 * In order to prevent compiler from being confused when using lambda, we renamed the method.
	 * this is similar to then except that the callback does not need to return anything.
	 * @param runOnWorkerThread if set to false, the action will run on the main thread (UI thread)
	 * specified by ThreadChanger. Otherwise the task will run on any other thread (worker thread).
	 * @param <In> The input type of this action. Lambda will automatically set this template parameter.
	 * @param action the action to be added.
	 * @return this object, thus enabling method chaining.
	 */
	<In> ThisType thenConsume(boolean runOnWorkerThread, Consumer<In> action);


	/**
	 * Add a 'PureAction' object, or an "action", in this ChainStyle on the <strong>worker</strong> thread.
	 * @param action the action to be added.
	 * @param <In> The input type of this action. Lambda will automatically set this template parameter.
	 * @param <Out> The output type of this action. Lambda will automatically set this template parameter.
	 * @return this object, thus enabling method chaining.
	 */
	<In, Out> ThisType netThen(PureAction<In, Out> action);

	<Out> ThisType netThen(Producer<Out> action);


	/**
	 * In order to prevent compiler from being confused when using lambda, we renamed the method.
	 * this is similar to then except that the callback does not need to return anything.
	 * @param <In> The input type of this action. Lambda will automatically set this template parameter.
	 * @param action the action to be added.
	 * @return this object, thus enabling method chaining.
	 */
	<In> ThisType netConsume(Consumer<In> action);

	/**
	 * Add a 'PureAction' object, or an "action", in this ChainStyle on the <strong>main (UI)</strong> thread.
	 * @param action the action to be added.
	 * @param <In> The input type of this action. Lambda will automatically set this template parameter.
	 * @param <Out> The output type of this action. Lambda will automatically set this template parameter.
	 * @return this object, thus enabling method chaining.
	 */
	<In, Out> ThisType uiThen(PureAction<In, Out> action);

	<Out> ThisType uiThen(Producer<Out> action);

	/**
	 * In order to prevent compiler from being confused when using lambda, we renamed the method.
	 * this is similar to then except that the callback does not need to return anything.
	 * @param action the action to be added.
	 * @param <In> The input type of this action. Lambda will automatically set this template parameter.
	 * @return this object, thus enabling method chaining.
	 */
	<In> ThisType uiConsume(Consumer<In> action);
}
