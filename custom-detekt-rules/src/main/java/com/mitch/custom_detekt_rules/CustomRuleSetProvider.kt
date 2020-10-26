/*
 * Copyright Tandem Diabetes Care, Inc. 2020. All rights reserved.
 * CloudAuth.kt
 * This class has a group of functions that maps to Larcus Cloud API authentication.
 * @author dlee Feb 28, 2018
 */

package com.mitch.custom_detekt_rules

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

class CustomRuleSetProvider : RuleSetProvider {
    override val ruleSetId: String = "custom-detekt-rules"

    override fun instance(config: Config)
        = RuleSet(ruleSetId, listOf(TooManyFunctions(), TOFIBRule()))
}
