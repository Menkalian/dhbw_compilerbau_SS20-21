package de.dhbw.mosbach.compilerbau;

import java.util.Objects;
import java.util.Set;

public class DFAState {
    public final int index;
    public final Boolean isAcceptingState;
    public final Set<Integer> positionsSet;

    public DFAState (int index, Boolean isAcceptingState, Set<Integer> positionsSet) {
        this.index = index;
        this.isAcceptingState = isAcceptingState;
        this.positionsSet = positionsSet;
    }

    @Override
    public boolean equals (Object obj) {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        DFAState that = (DFAState) obj;
        return Objects.equals(this.positionsSet, that.positionsSet);
    }

    @Override
    public int hashCode () {
        return this.positionsSet == null ? 0 : this.positionsSet.hashCode();
    }
}
