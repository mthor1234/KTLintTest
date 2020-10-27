/*
 * Copyright Tandem Diabetes Care, Inc. 2020. All rights reserved.
 * CloudAuth.kt
 * This class has a group of functions that maps to Larcus Cloud API authentication.
 * @author dlee Feb 28, 2018
 */

package com.mitch.custom_detekt_rules

import io.gitlab.arturbosch.detekt.api.*
import io.gitlab.arturbosch.detekt.rules.hasCommentInside
import org.jetbrains.kotlin.com.intellij.psi.PsiComment
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.psiUtil.startsWithComment


class TOFIBRule : Rule() {

    lateinit var currentFile: KtFile
    lateinit var currentFileName: String

    val copyright =
        "/*\n* Copyright Tandem Diabetes Care, Inc. 2020. All rights reserved.\n* CloudAuth.kt\n* This class has a group of functions that maps to Larcus Cloud API authentication.\n* @author dlee Feb 28, 2018\n*/"

    override val issue = Issue(
        javaClass.simpleName,
        Severity.CodeSmell,
        "This rule reports a file with an incorrect TOFIB.",
        Debt.FIVE_MINS
    )

//    private var amount: Int = 0

    override fun visitKtFile(file: KtFile) {
        super.visitKtFile(file)
        currentFile = file
        currentFileName = file.name

        System.out.println("Visiting File")
        System.out.println("${currentFile.text}")

        if (file.text.contains(copyright)) {
            System.out.println("Copyright Found")
        }

        file.children.let {

            System.out.println("Child Found")

            if (it is PsiComment) {
                System.out.println("Child Found: ${it.text}")
                checkTofibForCorrectness(it.text)
            }
        }
    }


    fun checkTofibForCorrectness(commentString: String) {

        report(
            CodeSmell(
                issue, Entity.atPackageOrFirstDecl(currentFile),
                message = "${commentString}"
            )
        )
    }

    fun parseFileName(tofibText: String): String? {
        println("parseFileName(): ${tofibText}")
        println("currentFIleName: ${currentFileName}")

        if (tofibText.contains(currentFileName)) {
            return currentFileName
        }
        return null
    }

    fun retrieveAuthorLine(tofibText: String) {
        val authorRegex = Regex("((\\* @author )([A-Z][a-z]+([ ]?[A-Z][a-z]+)))")
        authorRegex.find(tofibText)?.let {
            parseAuthor(it.value)
            parseCreationDate(it.value)
        }

    }

    fun parseAuthor(tofibText: String): String? {
        val firstLastRegex = Regex("([A-Z][a-z]+([ ]?[A-Z][a-z]+))")
        return firstLastRegex.find(tofibText)?.value ?: null
    }

    fun parseCreationDate(tofibText: String): String? {
        val creationDateRegex = Regex("([A-Z][a-z]{2} [0-2]?[0-9], [0-9]{4})")
        return creationDateRegex.find(tofibText)?.value ?: null
    }
}

