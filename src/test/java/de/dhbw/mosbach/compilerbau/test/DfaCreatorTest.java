package de.dhbw.mosbach.compilerbau.test;

import de.dhbw.mosbach.compilerbau.Parser;
import de.dhbw.mosbach.compilerbau.ast.BinOpNode;
import de.dhbw.mosbach.compilerbau.ast.OperandNode;
import de.dhbw.mosbach.compilerbau.ast.UnaryOpNode;
import de.dhbw.mosbach.compilerbau.dfa.DFAState;
import de.dhbw.mosbach.compilerbau.visit.FollowposTableEntry;
import de.dhbw.mosbach.compilerbau.util.CompareUtils;
import de.dhbw.mosbach.compilerbau.DFACreator;
import de.dhbw.mosbach.compilerbau.visit.Visitable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;

public class DfaCreatorTest {

    /**
     * Beispiel aus Kapitel 8 der Aufgabenstellung 
     */
    @Test
    public void testDFACreator_Skript()
    {      
        // create firstpos
        Set<Integer> positionsOfStartState = new HashSet<>(Arrays.asList(1,2,3)); 

        // create FollowposTable
        FollowposTableEntry row1 = new FollowposTableEntry(1, "a");
        row1.followpos.addAll(Arrays.asList(1, 2, 3));

        FollowposTableEntry row2 = new FollowposTableEntry(2, "b");
        row2.followpos.addAll(Arrays.asList(1, 2, 3));

        FollowposTableEntry row3 = new FollowposTableEntry(3, "a");
        row3.followpos.add(4);

        FollowposTableEntry row4 = new FollowposTableEntry(4, "b");
        row4.followpos.add(5);

        FollowposTableEntry row5 = new FollowposTableEntry(5, "b");
        row5.followpos.add(6);

        FollowposTableEntry row6 = new FollowposTableEntry(6, "#");
        
        List<FollowposTableEntry> list = Arrays.asList(row1, row2, row3, row4, row5, row6);

        SortedMap<Integer, FollowposTableEntry> followposTableEntries = new TreeMap<>();

        for (FollowposTableEntry i : list) {
            followposTableEntries.put(i.position, i);
        }

        // create expected stateTransitionTable
        Map<DFAState, Map<Character, DFAState>> stateTransitionTable = new HashMap<>(); 

        DFAState s1 = new DFAState(1, false, new HashSet<>(Arrays.asList(1,2,3)));
        DFAState s2 = new DFAState(2, false, new HashSet<>(Arrays.asList(1,2,3,4)));
        DFAState s3 = new DFAState(3, false, new HashSet<>(Arrays.asList(1,2,3,5)));
        DFAState s4 = new DFAState(4, true, new HashSet<>(Arrays.asList(1,2,3,6)));

        Map<Character, DFAState> innerEntries = new HashMap<>();
        innerEntries.put('a', s2);
        innerEntries.put('b', s1);
        stateTransitionTable.put(s1, innerEntries);

        innerEntries = new HashMap<>();
        innerEntries.put('a', s2);
        innerEntries.put('b', s3);
        stateTransitionTable.put(s2, innerEntries);

        innerEntries = new HashMap<>();
        innerEntries.put('a', s2);
        innerEntries.put('b', s4);
        stateTransitionTable.put(s3, innerEntries);

        innerEntries = new HashMap<>();
        innerEntries.put('a', s2);
        innerEntries.put('b', s1);
        stateTransitionTable.put(s4, innerEntries);

        // run DFACreator and compare
        DFACreator creator = new DFACreator(positionsOfStartState, followposTableEntries);
        creator.populateStateTransitionTable();      
        Assertions.assertEquals(stateTransitionTable, creator.getStateTransitionTable());
    }


    /**
     * Beispiel aus Protokoll MOS-TINF 19B 29_01_2021.pdf Seite 6
     */
    @Test
    public void testDFACreator_Skript_2()
    {      
        // create firstpos
        Set<Integer> positionsOfStartState = new HashSet<>(Arrays.asList(1,2,3)); 

        // create FollowposTable
        FollowposTableEntry row1 = new FollowposTableEntry(1, "a");
        row1.followpos.addAll(Arrays.asList(1, 2, 3));

        FollowposTableEntry row2 = new FollowposTableEntry(2, "b");
        row2.followpos.addAll(Arrays.asList(1, 2, 3));

        FollowposTableEntry row3 = new FollowposTableEntry(3, "c");
        row3.followpos.addAll(Arrays.asList(4,5));

        FollowposTableEntry row4 = new FollowposTableEntry(4, "d");
        row4.followpos.addAll(Arrays.asList(4,5));

        FollowposTableEntry row5 = new FollowposTableEntry(6, "#");
        
        List<FollowposTableEntry> list = Arrays.asList(row1, row2, row3, row4, row5);

        SortedMap<Integer, FollowposTableEntry> followposTableEntries = new TreeMap<>();

        for (FollowposTableEntry i : list) {
            followposTableEntries.put(i.position, i);
        }

        // create expected stateTransitionTable
        Map<DFAState, Map<Character, DFAState>> stateTransitionTable = new HashMap<>(); 

        DFAState s1 = new DFAState(1, false, new HashSet<>(Arrays.asList(1,2,3)));
        DFAState s2 = new DFAState(2, true, new HashSet<>(Arrays.asList(4,5)));

        Map<Character, DFAState> innerEntries = new HashMap<>();
        innerEntries.put('a', s1);
        innerEntries.put('b', s1);
        innerEntries.put('c', s2);
        innerEntries.put('d', null);
        stateTransitionTable.put(s1, innerEntries);

        innerEntries = new HashMap<>();
        innerEntries.put('a', null);
        innerEntries.put('b', null);
        innerEntries.put('c', null);
        innerEntries.put('d', s2);
        stateTransitionTable.put(s2, innerEntries);

        // run DFACreator and compare
        DFACreator creator = new DFACreator(positionsOfStartState, followposTableEntries);
        creator.populateStateTransitionTable();      
        Assertions.assertEquals(stateTransitionTable, creator.getStateTransitionTable());
    }


    /**
     * Selbst gewaehltes Beispiel
     */
    @Test
    public void testDFACreator_Selbst()
    {      
        // create firstpos
        Set<Integer> positionsOfStartState = new HashSet<>(Arrays.asList(1,2,3)); 

        // create FollowposTable
        FollowposTableEntry row1 = new FollowposTableEntry(1, "a");
        row1.followpos.addAll(Arrays.asList(1, 2, 3));

        FollowposTableEntry row2 = new FollowposTableEntry(2, "b");
        row2.followpos.addAll(Arrays.asList(1, 2, 3));

        FollowposTableEntry row3 = new FollowposTableEntry(3, "c");
        row3.followpos.addAll(Arrays.asList(4));

        FollowposTableEntry row4 = new FollowposTableEntry(4, "d");
        row4.followpos.addAll(Arrays.asList(5,6));

        FollowposTableEntry row5 = new FollowposTableEntry(5, "e");
        row5.followpos.addAll(Arrays.asList(5,6));

        FollowposTableEntry row6 = new FollowposTableEntry(6, "#");
        
        List<FollowposTableEntry> list = Arrays.asList(row1, row2, row3, row4, row5, row6);

        SortedMap<Integer, FollowposTableEntry> followposTableEntries = new TreeMap<>();

        for (FollowposTableEntry i : list) {
            followposTableEntries.put(i.position, i);
        }

        // create expected stateTransitionTable
        Map<DFAState, Map<Character, DFAState>> stateTransitionTable = new HashMap<>(); 

        DFAState s1 = new DFAState(1, false, new HashSet<>(Arrays.asList(1,2,3)));
        DFAState s2 = new DFAState(2, false, new HashSet<>(Arrays.asList(4)));
        DFAState s3 = new DFAState(3, true, new HashSet<>(Arrays.asList(5,6)));

        Map<Character, DFAState> innerEntries = new HashMap<>();
        innerEntries.put('a', s1);
        innerEntries.put('b', s1);
        innerEntries.put('c', s2);
        innerEntries.put('d', null);
        innerEntries.put('e', null);
        stateTransitionTable.put(s1, innerEntries);

        innerEntries = new HashMap<>();
        innerEntries.put('a', null);
        innerEntries.put('b', null);
        innerEntries.put('c', null);
        innerEntries.put('d', s3);
        innerEntries.put('e', null);
        stateTransitionTable.put(s2, innerEntries);

        innerEntries = new HashMap<>();
        innerEntries.put('a', null);
        innerEntries.put('b', null);
        innerEntries.put('c', null);
        innerEntries.put('d', null);
        innerEntries.put('e', s3);
        stateTransitionTable.put(s3, innerEntries);

        // run DFACreator and compare
        DFACreator creator = new DFACreator(positionsOfStartState, followposTableEntries);
        creator.populateStateTransitionTable();      
        Assertions.assertEquals(stateTransitionTable, creator.getStateTransitionTable());
    }
}
