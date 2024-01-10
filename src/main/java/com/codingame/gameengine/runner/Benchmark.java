package com.codingame.gameengine.runner;
import java.io.*;

import com.codingame.gameengine.runner.MultiplayerGameRunner;
import com.codingame.gameengine.runner.simulate.GameResult;
import com.google.common.io.Files;

public class Benchmark {

    static final long[] seeds = {
        -4104914489841135000L, // 6 monsters
        -136921930653170510L,
        -3834549125413743600L,
        7584300927533254000L,
        -3305779560099507000L,
        -7388364948627569000L,
        -4059022337247992000L,

        6847474064296349000L, // 4 monsters
        3424500372539279400L,
        -5699421973052809000L,
        9137309833376766000L,
        7766665962704826000L,
        1364479567551645000L,
        -6197757185483860000L, //frigthen my own targets

        3764215552269257000L, // 2 monsters
        -3318653557018802700L,
        3230334453862489600L,
        -1716645653449999600L,
        -5420256087014334000L,
        4154877989371618300L,
        -5185569762265297000L
    };
    public static void main(String[] args){

        File dir = new File("benchmark");
        int totalNbGames = 0;
        int totalWin = 0;
        MultiplayerGameRunner gameRunner;

        for (String f : dir.list()) {
            if (f.contains(".cpp")) continue;
            
            System.out.println("\u001B[0m " + f + " : ");
            for (long seed : seeds){    
                gameRunner = new MultiplayerGameRunner();
                gameRunner.setSeed(seed);
                gameRunner.addAgent("bots/seabed", "current");
                gameRunner.addAgent("benchmark/" + f, "x");
                gameRunner.setLeagueLevel(4);
                GameResult res = gameRunner.simulate();
                int myScore = res.scores.get(0);
                int oppScore = res.scores.get(1);

                String title = seed + "";
                String summary = String.format("%1$-30s : %2$3d / %3$3d", title, myScore, oppScore);

                totalNbGames++;
                if (myScore > oppScore){
                    System.out.println("  \u001B[32m" + summary + " - W");
                    totalWin++;
                } else if (myScore == oppScore){
                    System.out.println("  \u001B[33m" + summary + " - D");
                }
                else {
                    System.out.println("  \u001B[31m" + summary + " - L");
                }

                gameRunner = new MultiplayerGameRunner();
                gameRunner.setSeed(seed);
                gameRunner.addAgent("benchmark/" + f, "x");
                gameRunner.addAgent("bots/seabed", "current");
                gameRunner.setLeagueLevel(4);
                res = gameRunner.simulate();
                myScore = res.scores.get(1);
                oppScore = res.scores.get(0);

                title = seed + "(inv)";
                summary = String.format("%1$-30s : %2$3d / %3$3d", title, myScore, oppScore);

                totalNbGames++;
                if (myScore > oppScore){
                    System.out.println("  \u001B[32m" + summary + " - W");
                    totalWin++;
                } else if (myScore == oppScore){
                    System.out.println("  \u001B[33m" + summary + " - D");
                }
                else {
                    System.out.println("  \u001B[31m" + summary + " - L");
                }



                // System.out.println(res.outputs.get(0));
                // System.out.println(res.outputs.get(1));
                // System.out.println(res.errors.get(0));
                // System.out.println(res.errors.get(1));
                // System.out.println(res.metadata);
                // System.out.println(res.summaries);
                // for (int i = 0; i < res.summaries.size(); i++) {
                //     System.out.println(i + ": " + res.summaries[i]);
                // }
            }
        }
        float winrate = (float)totalWin * 100 / (float)totalNbGames;
        System.out.println("\u001B[0mWin rate = " + winrate + "%");
    }
}
