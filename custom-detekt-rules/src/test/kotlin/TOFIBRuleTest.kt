import com.mitch.custom_detekt_rules.TOFIBRule
import io.gitlab.arturbosch.detekt.api.*
import io.gitlab.arturbosch.detekt.test.assertThat
import io.gitlab.arturbosch.detekt.test.lint
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNull
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtImportDirective
import org.junit.Assert
import org.junit.Test

/*
 * Copyright Tandem Diabetes Care, Inc. 2020. All rights reserved.
 * CloudAuth.kt
 * This class has a group of functions that maps to Larcus Cloud API authentication.
 * @author dlee Feb 28, 2018
 */

class TOFIBRuleTest() {

    val copyright = "/*\n* Copyright Tandem Diabetes Care, Inc. 2020. All rights reserved.\n* CloudAuth.kt\n* This class has a group of functions that maps to Larcus Cloud API authentication.\n* @author dlee Feb 28, 2018\n*/"

    val copyrightInitial = "/*\n* Copyright Tandem Diabetes Care, Inc. "
    val copyRightPostYear = ". All rights reserved."
    val copyrightClassName = "* ${javaClass.simpleName}.kt"
    val copyrightDescription = "* This class has a group of functions that maps to Larcus Cloud API authentication."
    val copyrightAuthor = "* @author Mitch Thornton Feb 28, 2018"


    // YYYY | YYYY-YYYY
    val copyrightYearsREGEX = Regex("(([0-9]{4})-([0-9]{4}))|([0-9]{4})")


    @Test
    fun `TOFIB RULE TEST`() {

        val tofibRule = TOFIBRule()

        val findings = tofibRule.lint("""
            $copyright
        """.trimIndent())
        assertThat(findings).hasSize(1)
    }

    @Test
    fun `TOFIB RULE Date TEST`() {
        val tofibRule = TOFIBRule()

        val copyrightExpressions = "${copyrightInitial}2020${copyRightPostYear}\n* Test.kt\n${copyrightDescription}\n${copyrightAuthor}"


        tofibRule.currentFileName = tofibRule.javaClass.simpleName
        assertEquals("Test.kt", tofibRule.parseFileName(copyrightExpressions))
    }

    @Test
    fun `parseAuthor() Normal String Expect Correct Author`() {
        val tofibRule = TOFIBRule()
        assertEquals("Mitch Thornton", tofibRule.parseAuthor(copyrightAuthor))
    }


    @Test
    fun `parseAuthor() Empty String Expect Null`() {
        val tofibRule = TOFIBRule()
        assertNull(tofibRule.parseAuthor(""))
    }

    @Test
    fun `parseAuthor() Only First Name Expect Null`() {
        val tofibRule = TOFIBRule()
        val copyrightAuthor = "* @author Mitch Feb 08, 2018"

        assertNull(tofibRule.parseAuthor(copyrightAuthor))
    }

    @Test
    fun `parseCreationDate() Day Formatted with 0 Will Not Return Null`() {
        val tofibRule = TOFIBRule()

        val copyrightAuthor = "* @author Mitch Thornton Feb 08, 2018"
        assertEquals("Feb 08, 2018", tofibRule.parseCreationDate(copyrightAuthor))
    }


    @Test
    fun `parseCreationDate() Day Formatted With Two Digits Will Not Return Null`() {
        val tofibRule = TOFIBRule()

        val copyrightAuthor = "* @author Mitch Thornton Feb 28, 2018"
        assertEquals("Feb 28, 2018", tofibRule.parseCreationDate(copyrightAuthor))
    }

    @Test
    fun `parseCreationDate() Formatted MMDDYYYY Will Return Null`() {
        val tofibRule = TOFIBRule()

        val copyrightAuthor = "* @author Mitch Thornton 08/28/2018"
        assertNull(tofibRule.parseCreationDate(copyrightAuthor))
    }

    @Test
    fun `parseCreationDate() Without Date Will Return Null`() {
        val tofibRule = TOFIBRule()

        val copyrightAuthor = "* @author Mitch Thornton "
        assertNull(tofibRule.parseCreationDate(copyrightAuthor))
    }
}

