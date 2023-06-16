package com.amolina.epic.domain.testing

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestRule
import org.junit.runner.Description
import kotlin.coroutines.ContinuationInterceptor

@ExperimentalCoroutinesApi
class MainCoroutineRule : TestRule, TestCoroutineScope by TestCoroutineScope() {

  val dispatcher = coroutineContext[ContinuationInterceptor] as TestCoroutineDispatcher

  override fun apply(base: org.junit.runners.model.Statement, description: Description): org.junit.runners.model.Statement {
    return object : org.junit.runners.model.Statement() {
      @Throws(Throwable::class)
      override fun evaluate() {

        Dispatchers.setMain(dispatcher)

        // everything above this happens before the test
        base.evaluate()
        // everything below this happens after the test

        cleanupTestCoroutines()
        Dispatchers.resetMain()
      }
    }
  }
}