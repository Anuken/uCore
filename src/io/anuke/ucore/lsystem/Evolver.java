package io.anuke.ucore.lsystem;

import io.anuke.ucore.util.Mathf;

import java.util.HashMap;

public class Evolver{
    public static boolean debug = false;

    HashMap<Character, String> current = new HashMap<>();
    float currentSpace;
    float currentScore;

    public LSystemData[] evolve(EvolutionData data, int amount){
        LSystemData[] result = new LSystemData[amount];

        for(int i = 0; i < amount; i++){
            result[i] = evolve(data);
        }

        return result;
    }

    public LSystemData evolve(EvolutionData data){
        current.clear();
        current.putAll(data.defaultRules);
        currentSpace = data.defaultSpace;
        currentScore = 0f;

        for(int g = 0; g < data.generations; g++){

            HashMap<Character, String> bestTree = current;
            float bestSpace = currentSpace;
            float bestScore = currentScore;

            for(int i = 0; i < data.variants; i++){
                HashMap<Character, String> mutation = mutateCurrent(data);
                float mutSpace = currentSpace + (data.changeSpace ? Mathf.range(5f) : 0f);

                LTree tree = LProcessor.getLines(data.axiom, mutation, data.iterations, mutSpace);

                float score = data.eval.getScore(tree);

                if(score < 0){
                    continue;
                }

                if(score > bestScore){
                    bestTree = mutation;
                    bestScore = score;
                    bestSpace = mutSpace;
                }
            }

            current = bestTree;
            currentSpace = bestSpace;
            currentScore = bestScore;
        }

        //result.result = LGen.gen(result.axiom, result.rules, result.iterations);
        return new LSystemData(data.axiom, (HashMap<Character, String>) current.clone(), data.iterations, data.swayspace,
                data.swayscale, data.swayphase, data.length, currentSpace, data.thickness, data.start, data.end);
    }

    HashMap<Character, String> mutateCurrent(EvolutionData data){
        HashMap<Character, String> map = (HashMap<Character, String>) current.clone();

        map.put('X', mutateString(map.get('X'), data));

        return map;
    }

    String mutateString(String in, EvolutionData data){
        int mutations = Mathf.random(1, data.maxMutations);
        StringBuilder current = new StringBuilder(in);

        for(int i = 0; i < mutations; i++){

            int rand = Mathf.random(0, data.insertChars.length + 5);

            // delete a random character
            if(Mathf.chance(0.2) && current.length() > 5){
                int idx = Mathf.random(current.length() - 1);
                current.deleteCharAt(idx);
                continue;
            }

            if(data.limitrulesize && current.length() > data.maxrulesize){
                // can only change a character, so just delete here, and let other code add
                if(rand < data.insertChars.length){
                    int idx = Mathf.random(current.length() - 1);
                    current.deleteCharAt(idx);
                }else if((rand <= data.insertChars.length + 1 && current.length() > 1) ||
                        (current.length() > 4)){
                    int idx = Mathf.random(current.length() - 1);
                    current.deleteCharAt(idx);

                    idx = Mathf.random(current.length() - 1);
                    current.deleteCharAt(idx);
                }
            }

            if(rand < data.insertChars.length){ // insert a random character
                current.insert(Mathf.random(0, current.length() - 1), data.insertChars[rand]);
            }else if(rand <= data.insertChars.length + 1 && current.length() > 1){

                current.insert(Mathf.random(0, current.length() - 1), '-');
                current.insert(Mathf.random(0, current.length() - 1), '+');

            }else if(current.length() > 4){
                int idx = Mathf.random(0, current.length() - 3);
                current.insert(idx, '[');
                current.insert(Mathf.random(idx + 1, current.length() - 1), ']');
            }

        }

        return current.toString();
    }
}
