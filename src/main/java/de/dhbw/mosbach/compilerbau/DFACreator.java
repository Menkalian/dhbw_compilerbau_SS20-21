package de.dhbw.mosbach.compilerbau;

import de.dhbw.mosbach.compilerbau.dfa.DFAState;
import de.dhbw.mosbach.compilerbau.visit.FollowposTableEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class DFACreator {
    private final Set<Integer> positionsForStartState;
    private final SortedMap<Integer, FollowposTableEntry> followposTable;
    private final Map<DFAState, Map<Character, DFAState>> stateTransitionTable;
    private int counter = 1;

    /**
     * Man beachte ! Parameter <code>positionsForStartState</code> muss vom Aufrufer
     * mit der firstpos-Menge des Wurzelknotens des Syntaxbaums initialisiert werden !
     */
    public DFACreator (Set<Integer> positionsForStartState,
                       SortedMap<Integer, FollowposTableEntry> followposTable) {
        this.positionsForStartState = positionsForStartState;
        this.followposTable = followposTable;
        this.stateTransitionTable = new HashMap<>();
    }

    // befuellt die Uebergangsmatrix
    public void populateStateTransitionTable () {

        // Alphabet ermitteln
        Set<String> alphabet = new HashSet<>();
        for (FollowposTableEntry entry: followposTable.values())
        {
            if (!entry.symbol.equals("#"))
            {
                alphabet.add(entry.symbol);
            }
        }

        // eine Liste mit Startzustand initialisieren
        List<DFAState> qStates = new ArrayList<>();
        int posOfTerminatingSymbol = followposTable.lastKey(); // Schluessel des letzten Eintrags
        DFAState startState = new DFAState(
            counter++,
            positionsForStartState.contains(posOfTerminatingSymbol),
            positionsForStartState
        );
        qStates.add(startState);

        // Algorithmus durchlaufen
        Map<Character, DFAState> innerCell;
        while (!qStates.isEmpty())
        {
            // ersten Zustand aus qstates entnehmen und entfernen
            DFAState currentState = qStates.get(0);
            qStates.remove(0);

            // neue Zeile in stateTransitionTable hinzufuegen mit currentState als Schluessel
            innerCell = new HashMap<>();
            stateTransitionTable.put(currentState, innerCell);

            for (String symbol: alphabet)
            {
                // Folgezustand ermitteln + einfügen in neue Zeile
                DFAState followState = calculateFollowState(currentState, symbol);
                innerCell.put(symbol.charAt(0), followState);

                // Ueberpruefen, ob Folgezustand schon erfasst bzw. verarbeitet wurde
                // Falls nicht, am Ende von qStates hinzufuegen
                if(followState!=null                    
                    && !stateTransitionTable.containsKey(followState)
                    && !qStates.contains(followState))
                {
                    qStates.add(followState);
                }
            }        
        }
    }

    private DFAState calculateFollowState(DFAState s, String a){
        
        Set<Integer> nextPositionsSet = new HashSet<>();
        
        for (FollowposTableEntry entry : followposTable.values())
        {
            if(s.positionsSet.contains(entry.position) && entry.symbol.equals(a))
            {
                nextPositionsSet.addAll(entry.followpos);
            }
        }

        if(nextPositionsSet.isEmpty())
        {
            return null;
        }

        boolean isAcceptingState = nextPositionsSet.contains(followposTable.lastKey());  //Akzeptierter Zustand, wenn nextPositionsSet die Position von # enthält 
        
        return new DFAState(counter++, isAcceptingState, nextPositionsSet);
    }

    public Map<DFAState, Map<Character, DFAState>> getStateTransitionTable () {
        return stateTransitionTable;
    }
}
