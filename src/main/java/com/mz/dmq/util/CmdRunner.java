package com.mz.dmq.util;

import lombok.SneakyThrows;
import org.javatuples.Triplet;

import java.util.List;

public class CmdRunner {
    private final String[] cmd;

    public CmdRunner(List<String> cmd) {
        this.cmd = cmd.toArray(new String[]{});
    }

    @SneakyThrows
    public Triplet<Integer, String, String> runSync() {
        Process process = Runtime.getRuntime().exec(cmd);
        int exitCode = process.waitFor();
        String stdout = new String(process.getInputStream().readAllBytes());
        String stderr = new String(process.getErrorStream().readAllBytes());
        return Triplet.with(exitCode, stdout, stderr);
    }
}
