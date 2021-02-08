package de.dhbw.mosbach.compilerbau;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class FollowposTableEntry {
    public final int position;
    public final String symbol;
    public final Set<Integer> followpos = new HashSet<>();

    public FollowposTableEntry (int position, String symbol) {
        this.position = position;
        this.symbol = symbol;
    }

    @Override
    public boolean equals (Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof FollowposTableEntry)) {
            return false;
        }

        FollowposTableEntry other = (FollowposTableEntry) obj;
        return position == other.position &&
               Objects.equals(symbol, other.symbol) && // Use Objects.equals for null-safety
               Objects.equals(followpos, other.followpos);
    }

    @Override
    public int hashCode () {
        int hashCode = this.position;
        hashCode = 31 * hashCode + this.symbol.hashCode();
        hashCode = 31 * hashCode + this.followpos.hashCode();
        return hashCode;
    }
}
