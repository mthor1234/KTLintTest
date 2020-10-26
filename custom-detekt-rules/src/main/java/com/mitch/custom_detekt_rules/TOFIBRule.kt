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

        if (!file.startsWithComment()) {

            file.children.forEach {
                if(it is PsiComment){
                    report(
                        CodeSmell(
                            issue, Entity.atPackageOrFirstDecl(currentFile),
                            message = "Comment Found"
                        )
                    )
                }

            }

            report(
                CodeSmell(
                    issue, Entity.atPackageOrFirstDecl(file),
                    message = "File doesn't start with a TOFIB"
                )
            )
        }
    }


//    override fun visitComment(comment: PsiComment?) {
//        super.visitComment(comment)
//        report(
//            CodeSmell(
//                issue, Entity.atPackageOrFirstDecl(currentFile),
//                message = "Comment Found"
//            )
//        )
//    }



    fun readComment(file: KtFile){
        file.hasCommentInside()

    }
}

