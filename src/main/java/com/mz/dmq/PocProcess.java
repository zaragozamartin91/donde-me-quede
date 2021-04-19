package com.mz.dmq;

import com.mz.dmq.util.CmdRunner;
import org.javatuples.Triplet;

import java.io.IOException;
import java.util.List;

public class PocProcess {
    public static void main(String[] args) throws IOException, InterruptedException {
        String[] cmdarr = {"cmd", "/c", "docker-machine", "ip"};
        Triplet<Integer, String, String> result = new CmdRunner(
                List.of("cmd", "/c", "docker-machine", "ip")
        ).runSync();
        System.out.println(result);
//        Process process = Runtime.getRuntime().exec(cmdarr);
//        int exitCode = process.waitFor();
//        System.out.println("Exit code is " + exitCode);
//        System.out.println(new String(process.getInputStream().readAllBytes()));
    }
}
