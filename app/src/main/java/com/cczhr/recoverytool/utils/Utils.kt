package com.cczhr.recoverytool.utils

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

/**
 * @author cczhr
 * @since  2020/9/6
 * @description https://github.com/cczhr
 */
val RECOVERY_PATH = "/cache/recovery/"
val TWRP_FILE = "openrecoveryscript"
val COMMAND_FILE = "command"
val TWRP_COMMAND= arrayOf("install","decrypt","wipe data","wipe cache","wipe system")
val COMMOM_COMMAND= arrayOf("update_package","wipe_data","wipe_cache")
fun runCommand(command: String) {
    Runtime.getRuntime().exec("su").apply {
        outputStream.apply {
            write(command.toByteArray())
            flush()
            close()
        }
    }
}

fun deleteFile(vararg paths: String) {
    paths.forEach {
        runCommand("rm $it")
    }
}

fun moveFile(source: String, dest: String) {
    runCommand("mv $source $dest")
}

fun rebootRecovery() {
    runCommand("reboot recovery")
}

fun deleteRecoveryFile() {
    deleteFile(RECOVERY_PATH + TWRP_FILE, RECOVERY_PATH + COMMAND_FILE)
}

fun writeAndRestartRecovery(context: Context, fileName: String, command: String) {
    GlobalScope.launch(Dispatchers.IO) {
        deleteRecoveryFile()
        val source = context.filesDir.absolutePath + File.separator + fileName
        val dest = RECOVERY_PATH
        File(source).writeText(command)
        moveFile(source, dest)
        rebootRecovery()
    }
}