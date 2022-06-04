package com.aueb.idry.T8816WP;

import com.aueb.idry.R;

/**
 * An enum that holds every drying level of the dryer, along with the level's string representation ID.
 */
public enum DryingLevel {
    EXTRA_DRY {
        @Override
        public int getStringId() {
            return R.string.drying_level_extra_dry_btn;
        }
    },
    NORMAL {
        @Override
        public int getStringId() {
            return R.string.drying_level_normal_btn;
        }
    },
    HAND_IRON {
        @Override
        public int getStringId() {
            return R.string.drying_level_hand_iron_btn;
        }
    },
    MACHINE_IRON {
        @Override
        public int getStringId() {
            return R.string.drying_level_machine_iron_btn;
        }
    };

    /**
     * Return the string representation of the enum entry.
     * @return the string ID for this enum entry
     */
    public abstract int getStringId();
}
