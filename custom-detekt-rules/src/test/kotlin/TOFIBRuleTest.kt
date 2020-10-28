import com.mitch.customDetektRules.Constants.Companion.COPYRIGHT
import com.mitch.customDetektRules.TOFIBRule
import io.gitlab.arturbosch.detekt.test.assertThat
import io.gitlab.arturbosch.detekt.test.lint
import junit.framework.Assert.*
import org.junit.Test

/*
 * Copyright Mitchell James Thornton, Inc. 2020. All rights reserved.
 * TOFIBRuleTest.kt
 * This class has a group of functions that maps to Larcus Cloud API authentication.
 * @author Mitchell Thornton Feb 28, 2018
 */

class TOFIBRuleTest() {

    val copyrightInitial = "/*\n * Copyright Mitchell James Thornton, Inc. "
    val copyRightPostYear = ". All rights reserved."
    val copyrightClassName = "* ${javaClass.simpleName}.kt"
    val copyrightDescription = "* This class has a group of functions that maps to the Cloud API authentication."
    val copyrightAuthor = " * @author Mitch Thornton Feb 28, 2018"

    val correctFirstLine = " * Copyright Mitchell James Thornton, Inc. 2020. All rights reserved."
    val correctFirstLineHyphen = " * Copyright Mitchell James Thornton, Inc. 2018-2020. All rights reserved."
    val correctClassName = " * TOFIBRule.kt"
    val correctDescriptionLine = " * This is a test description.... Periods, ___, or | Should work"

    // YYYY | YYYY-YYYY
    val copyrightYearsREGEX = Regex("(([0-9]{4})-([0-9]{4}))|([0-9]{4})")

    ////// Update Line //////
    @Test
    fun `checkUpdateLine() Correct Update Line String Expect True`() {
        val tofibRule = TOFIBRule()
        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertTrue(tofibRule.checkUpdateLine(correctFirstLine))
    }

    @Test
    fun `checkUpdateLine() Correct Update Line With Hyphen String Expect True`() {
        val tofibRule = TOFIBRule()
        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertTrue(tofibRule.checkUpdateLine(correctFirstLineHyphen))
    }

    @Test
    fun `checkUpdateLine() InCorrect Update Line With One space Before Hyphen String Expect False`() {

        val updateLineHyphenWSpaces = " * Copyright Mitchell James Thornton, Inc. 2018 -2020. All rights reserved."
        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertFalse(tofibRule.checkUpdateLine(updateLineHyphenWSpaces))
    }

    @Test
    fun `checkUpdateLine() InCorrect Update Line With One space After Hyphen String Expect False`() {

        val updateLineHyphenWSpaces = " * Copyright Mitchell James Thornton, Inc. 2018- 2020. All rights reserved."
        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertFalse(tofibRule.checkUpdateLine(updateLineHyphenWSpaces))
    }

    @Test
    fun `checkUpdateLine() InCorrect Update Line With Spaces Bothsides Hyphen String Expect False`() {

        val updateLineHyphenWSpaces = " * Copyright Mitchell James Thornton, Inc. 2018 - 2020. All rights reserved."
        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertFalse(tofibRule.checkUpdateLine(updateLineHyphenWSpaces))
    }

    @Test
    fun `checkUpdateLine() InCorrect Update Line With Extra Beinginning White-Space String Expect False`() {

        val updateLineHyphenWSpaces = "  * Copyright Mitchell James Thornton, Inc. 2018 - 2020. All rights reserved."
        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertFalse(tofibRule.checkUpdateLine(updateLineHyphenWSpaces))
    }


    @Test
    fun `checkUpdateLine() InCorrect Update Line With No * Expect False`() {

        val updateLineHyphenWSpaces = "Copyright Mitchell James Thornton, Inc. 2018 - 2020. All rights reserved."
        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertFalse(tofibRule.checkUpdateLine(updateLineHyphenWSpaces))
    }


    @Test
    fun `checkUpdateLine() InCorrect Update Line With No Copyright Expect False`() {

        val updateLineHyphenWSpaces = " *  Mitchell James Thornton, Inc. 2018 - 2020. All rights reserved."
        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertFalse(tofibRule.checkUpdateLine(updateLineHyphenWSpaces))
    }


    @Test
    fun `checkUpdateLine() InCorrect Update Line With No All rights reserved Expect False`() {

        val updateLineHyphenWSpaces = " *  Mitchell James Thornton, Inc. 2018 - 2020. "
        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertFalse(tofibRule.checkUpdateLine(updateLineHyphenWSpaces))
    }

    @Test
    fun `checkUpdateLine() InCorrect Update Line With Extra Space At End Expect False`() {
        val firstLineExtraSpace = " * Copyright Mitchell James Thornton, Inc. 2018-2020. All rights reserved. "
        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertTrue(tofibRule.checkUpdateLine(firstLineExtraSpace))
    }


    ////// File Name //////
    @Test
    fun `checkFileName() Correct Filename Line Expect True`() {
        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertTrue(tofibRule.checkFileName(correctClassName))
    }


    @Test
    fun `checkFileName() Incorrect Missing Filename Expect False`() {
        val incorrectFileNameLine = " * "

        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertFalse(tofibRule.checkFileName(incorrectFileNameLine))
    }


    @Test
    fun `checkFileName() Incorrect Missing kt Extension Expect False`() {
        val incorrectFileNameLine = " * TOFIBRule"

        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertFalse(tofibRule.checkFileName(incorrectFileNameLine))
    }


    @Test
    fun `checkFileName() Incorrect Missing * Expect False`() {
        val incorrectFileNameLine = "  TOFIBRule.kt"

        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertFalse(tofibRule.checkFileName(incorrectFileNameLine))
    }

    @Test
    fun `checkFileName() Incorrect Extra Space Before * Expect False`() {
        val incorrectFileNameLine = "  * TOFIBRule.kt"

        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertFalse(tofibRule.checkFileName(incorrectFileNameLine))
    }

    @Test
    fun `checkFileName() Incorrect Extra Space After * Expect False`() {
        val incorrectFileNameLine = " *  TOFIBRule.kt"

        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertFalse(tofibRule.checkFileName(incorrectFileNameLine))
    }


    @Test
    fun `checkFileName() Correct Extra Space At The End Expect True`() {
        val incorrectFileNameLine = " *  TOFIBRule.kt    "

        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertFalse(tofibRule.checkFileName(incorrectFileNameLine))
    }


    ////// File Description Line //////

    @Test
    fun `checkIfValidDescriptionLine() Correct Description Expect True`() {
        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertTrue(tofibRule.checkIfValidDescriptionLine(correctDescriptionLine))
    }

    @Test
    fun `checkIfValidDescriptionLine() Correct Description Extra Space After * Expect True`() {

        val descriptionLineMissingStar = " *  This is a test description.... Perioids, ___, or | Should work"
        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertTrue(tofibRule.checkIfValidDescriptionLine(descriptionLineMissingStar))
    }

    @Test
    fun `checkIfValidDescriptionLine() Correct Description Two Tabs After * Expect True`() {

        val descriptionLineMissingStar = " *        This is a test description.... Periods, ___, or | Should work"
        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertTrue(tofibRule.checkIfValidDescriptionLine(descriptionLineMissingStar))
    }

    @Test
    fun `checkIfValidDescriptionLine() Correct Description Line Elipses After * Expect True`() {

        val descriptionLineMissingStar = " * ......This is a test description.... Periods, ___, or | Should work"
        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertTrue(tofibRule.checkIfValidDescriptionLine(descriptionLineMissingStar))
    }


    @Test
    fun `checkIfValidDescriptionLine() Correct Description Line CAPS After * Expect True`() {

        val descriptionLineMissingStar = " * TESTING is a test description.... Periods, ___, or | Should work"
        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertTrue(tofibRule.checkIfValidDescriptionLine(descriptionLineMissingStar))
    }

    @Test
    fun `checkIfValidDescriptionLine() Correct Description Line Extra Spaces At End Expect True`() {

        val descriptionLineMissingStar = " * TESTING is a test description.... Periods, ___, or | Should work    "
        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertTrue(tofibRule.checkIfValidDescriptionLine(descriptionLineMissingStar))
    }


    @Test
    fun `checkIfValidDescriptionLine() Incorrect Description Missing * Expect False`() {

        val descriptionLineMissingStar = " This is a test description.... Periods, ___, or | Should work"
        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertFalse(tofibRule.checkIfValidDescriptionLine(descriptionLineMissingStar))
    }


    @Test
    fun `checkIfValidDescriptionLine() Incorrect Description Missing Space Before * Expect False`() {

        val descriptionLineMissingStar = "* This is a test description.... Periods, ___, or | Should work"
        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertFalse(tofibRule.checkIfValidDescriptionLine(descriptionLineMissingStar))
    }

    @Test
    fun `checkIfValidDescriptionLine() Incorrect Description Extra Space Before * Expect False`() {

        val descriptionLineMissingStar = "  * This is a test description.... Periods, ___, or | Should work"
        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertFalse(tofibRule.checkIfValidDescriptionLine(descriptionLineMissingStar))
    }


    ////// Description With Entire Copyright Fed //////



    @Test
    fun `checkTOFIBForDescription() TOFIB With Correct Description Expect True`() {

        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertTrue(tofibRule.checkTOFIBForDescription(COPYRIGHT))
    }

    @Test
    fun `checkTOFIBForDescription() TOFIB With Two Line Description Expect True`() {

        val TOFIBMultiLineDescription =
            "/*\n" +
                " * Copyright Mitchell James Thornton, Inc. 2020. All rights reserved.\n" +
                " * CloudAuth.kt\n" +
                " * This class has a group of functions that maps to Larcus Cloud API authentication.\n" +
                " * This class has a group of functions that maps to Larcus Cloud API authentication.\n" +
                " * @author Mitchell Thornton Feb 28, 2018\n" +
                "*/"


        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertTrue(tofibRule.checkTOFIBForDescription(TOFIBMultiLineDescription))
    }

    @Test
    fun `checkTOFIBForDescription() TOFIB With Ten Line Description Expect True`() {

        val TOFIBMultiLineDescription =
            "/*\n" +
                " * Copyright Mitchell James Thornton, Inc. 2020. All rights reserved.\n" +
                " * CloudAuth.kt\n" +
                " * This class has a group of functions that maps to Larcus Cloud API authentication.\n" +
                " * This class has a group of functions that maps to Larcus Cloud API authentication.\n" +
                " * This class has a group of functions that maps to Larcus Cloud API authentication.\n" +
                " * This class has a group of functions that maps to Larcus Cloud API authentication.\n" +
                " * This class has a group of functions that maps to Larcus Cloud API authentication.\n" +
                " * This class has a group of functions that maps to Larcus Cloud API authentication.\n" +
                " * This class has a group of functions that maps to Larcus Cloud API authentication.\n" +
                " * This class has a group of functions that maps to Larcus Cloud API authentication.\n" +
                " * This class has a group of functions that maps to Larcus Cloud API authentication.\n" +
                " * This class has a group of functions that maps to Larcus Cloud API authentication.\n" +
                " * @author Mitchell Thornton Feb 28, 2018\n" +
                "*/"

        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertTrue(tofibRule.checkTOFIBForDescription(TOFIBMultiLineDescription))
    }

    @Test
    fun `checkTOFIBForDescription() TOFIB With Two Line Description Tab Expect True`() {

        val TOFIBMultiLineDescription =
            "/*\n" +
                " * Copyright Mitchell James Thornton, Inc. 2020. All rights reserved.\n" +
                " * CloudAuth.kt\n" +
                " * This class has a group of functions that maps to Larcus Cloud API authentication.\n" +
                " *         This class has a group of functions that maps to Larcus Cloud API authentication.\n" +
                " * @author Mitchell Thornton Feb 28, 2018\n" +
                "*/"


        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertTrue(tofibRule.checkTOFIBForDescription(TOFIBMultiLineDescription))
    }


    @Test
    fun `checkTOFIBForDescription() TOFIB With Description Missing Expect False`() {

        val TOFIBDescriptionMissingStar =
            "/*\n" +
                " * Copyright Mitchell James Thornton, Inc. 2020. All rights reserved.\n" +
                " * CloudAuth.kt\n" +
                " * @author Mitchell Thornton Feb 28, 2018\n" +
                "*/"

        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertFalse(tofibRule.checkIfValidDescriptionLine(TOFIBDescriptionMissingStar))
    }


    @Test
    fun `checkTOFIBForDescription() TOFIB With Description Missing * Expect False`() {

        val TOFIBDescriptionMissingStar =
            "/*\n" +
                " * Copyright Mitchell James Thornton, Inc. 2020. All rights reserved.\n" +
                " * CloudAuth.kt\n" +
                " This class has a group of functions that maps to Larcus Cloud API authentication.\n" +
                " * @author Mitchell Thornton Feb 28, 2018\n" +
                "*/"


        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertFalse(tofibRule.checkIfValidDescriptionLine(TOFIBDescriptionMissingStar))
    }

    ////// Author Line //////

    @Test
    fun `checkAuthorLine() Correct Author Line Expect True`() {

        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertTrue(tofibRule.checkAuthorLine(copyrightAuthor))
    }

    @Test
    fun `checkAuthorLine() Incorrect Author Line Missing Space Before * Expect False`() {

        val copyrightAuthor = "* @author Mitch Thornton Feb 28, 2018"

        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertFalse(tofibRule.checkAuthorLine(copyrightAuthor))
    }


    @Test
    fun `checkAuthorLine() Incorrect Author Line Extra Space After * Expect False`() {

        val copyrightAuthor = "*  @author Mitch Thornton Feb 28, 2018"

        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertFalse(tofibRule.checkAuthorLine(copyrightAuthor))
    }


    @Test
    fun `checkAuthorLine() Incorrect Author Line Missing * Expect False`() {

        val copyrightAuthor = " @author Mitch Thornton Feb 28, 2018"

        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertFalse(tofibRule.checkAuthorLine(copyrightAuthor))
    }

    @Test
    fun `checkAuthorLine() Incorrect Author Line Missing @author Expect False`() {

        val copyrightAuthor = " * Mitch Thornton Feb 28, 2018"

        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertFalse(tofibRule.checkAuthorLine(copyrightAuthor))
    }

    @Test
    fun `checkAuthorLine() Incorrect Author Line Missing First Name Expect False`() {

        val copyrightAuthor = " * @author Thornton Feb 28, 2018"

        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertFalse(tofibRule.checkAuthorLine(copyrightAuthor))
    }


    @Test
    fun `checkAuthorLine() Incorrect Author First Initial Dot Last Name Expect False`() {

        val copyrightAuthor = " * @author M.Thornton Feb 28, 2018"

        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertFalse(tofibRule.checkAuthorLine(copyrightAuthor))
    }


    @Test
    fun `checkAuthorLine() Incorrect Missing Month Expect False`() {

        val copyrightAuthor = " * @author Mitch Thornton 28, 2018"

        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertFalse(tofibRule.checkAuthorLine(copyrightAuthor))
    }

    @Test
    fun `checkAuthorLine() Incorrect Month Format Slashes Expect False`() {

        val copyrightAuthor = " * @author Mitch Thornton 9/28/2018"

        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertFalse(tofibRule.checkAuthorLine(copyrightAuthor))
    }

    @Test
    fun `checkAuthorLine() Incorrect Month Format Periods Expect False`() {

        val copyrightAuthor = " * @author Mitch Thornton 9.28.2018"

        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())
        assertFalse(tofibRule.checkAuthorLine(copyrightAuthor))
    }



    // Older tests.. Need to double check
    @Test
    fun `parseAuthor() Normal String Expect Correct Author`() {
        val tofibRule = TOFIBRule()
        assertEquals("Mitch Thornton", tofibRule.parseAuthor(copyrightAuthor))
    }


    @Test
    fun `parseAuthor() Empty String Expect Null`() {
        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())

        assertNull(tofibRule.parseAuthor(""))
    }

    @Test
    fun `parseCreationDate() Day Formatted with 0 Will Not Return Null`() {
        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())

        val copyrightAuthor = "* @author Mitch Thornton Feb 08, 2018"
        assertEquals("Feb 08, 2018", tofibRule.parseCreationDate(copyrightAuthor))
    }


    @Test
    fun `parseCreationDate() Day Formatted With Two Digits Will Not Return Null`() {
        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())

        val copyrightAuthor = "* @author Mitch Thornton Feb 28, 2018"
        assertEquals("Feb 28, 2018", tofibRule.parseCreationDate(copyrightAuthor))
    }

    @Test
    fun `parseCreationDate() Formatted MMDDYYYY Will Return Null`() {
        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())

        val copyrightAuthor = "* @author Mitch Thornton 08/28/2018"
        assertNull(tofibRule.parseCreationDate(copyrightAuthor))
    }

    @Test
    fun `parseCreationDate() Without Date Will Return Null`() {
        val tofibRule = TOFIBRule()

        tofibRule.lint("""
            $COPYRIGHT
        """.trimIndent())

        val copyrightAuthor = "* @author Mitch Thornton "
        assertNull(tofibRule.parseCreationDate(copyrightAuthor))
    }

    @Test
    fun `parseCreationUpdateYears() Creation Year Only Expect |CreationYear|null|`() {
        val tofibRule = TOFIBRule()
        val primaryCopyrightString = " * Copyright Mitchell Thornton Co, Inc. 2018. All rights reserved."
        val retrievedCreationYear = tofibRule.parseYearRange(primaryCopyrightString)[0]
        assertEquals("2018", retrievedCreationYear)
    }


    @Test
    fun `parseCreationUpdateYears() Creation Year & Updated Year Expect |creationYear|updatedYear|`() {
        val tofibRule = TOFIBRule()
        val primaryCopyrightString = " * Copyright Mitchell Thornton Co, Inc. 2018-2020. All rights reserved. "
        val retrievedCreationYear = tofibRule.parseYearRange(primaryCopyrightString)[0]
        val retrievedUpdateYear = tofibRule.parseYearRange(primaryCopyrightString)[1]
        assertEquals("2018", retrievedCreationYear)
        assertEquals("2020", retrievedUpdateYear)
    }

}

