package de.dhbw.mosbach.compilerbau;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class DFACreator {
    private final Set<Integer> positionsForStartState;
    private final SortedMap<Integer, FollowposTableEntry> followposTable;
    private final Map<DFAState, Map<Character, DFAState>> stateTransitionTable;

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
        // TODO: 08.02.2021 ...
    }

    public Map<DFAState, Map<Character, DFAState>> getStateTransitionTable () {
        return stateTransitionTable;
    }

    // TODO: 08.02.2021 ...
}
