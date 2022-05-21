package com.aueb.idry.T8816WP;

import com.aueb.idry.R;

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

    public abstract int getStringId();
}
