// (c) Facebook, Inc. and its affiliates. Confidential and proprietary.

package com.facebook.ktfmt

import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.KtQualifiedExpression
import org.jetbrains.kotlin.psi.KtReferenceExpression

/**
 * A list of common package name initial domain names This is used in order to avoid splitting
 * package names when possible
 */
private val packageNames = setOf("com", "gov", "java", "javax", "kotlin", "org")

/**
 * Guesses the amount of parts in a qualified expression that might represent a type name
 *
 * Examples:
 * ```
 * com.facebook.ktfmt.Formatter.doIt() -> 3
 * Formatter.doIt() -> 0
 * myObject.field.anotherField = 0
 * ```
 */
fun getTypePrefixLength(expression: KtQualifiedExpression): Int {
  var current: KtExpression = expression
  var count = 0
  while (current is KtQualifiedExpression) {
    val selectorExpression = current.selectorExpression
    if (selectorExpression is KtCallExpression ||
        (selectorExpression is KtReferenceExpression &&
            selectorExpression.text?.first()?.isUpperCase() == true)) {
      count = 0
    }
    count++
    current = current.receiverExpression
  }
  return if ((current as? KtReferenceExpression)?.text in packageNames) count else 0
}
