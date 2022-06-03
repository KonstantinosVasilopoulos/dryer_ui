package com.aueb.idry.T8816WP;

import com.aueb.idry.R;

/**
 * An enum which has every programme available to the dryer and the programme's string ID.
 *
 * @author Konstantinos Vasilopoulos
 */
public enum Programme {
    COTTONS {
        @Override
        public int getStringId() {
            return R.string.programme_cottons_btn;
        }
    },
    MINIMUM_IRON {
        @Override
        public int getStringId() {
            return R.string.programme_minimum_iron_btn;
        }
    },
    WOOLLENS {
        @Override
        public int getStringId() {
            return R.string.programme_woollens_btn;
        }
    },
    OUTERWEAR {
        @Override
        public int getStringId() {
            return R.string.programme_outerwear_btn;
        }
    },
    PROOFING {
        @Override
        public int getStringId() {
            return R.string.programme_proofing_btn;
        }
    },
    EXPRESS {
        @Override
        public int getStringId() {
            return R.string.programme_express_btn;
        }
    },
    AUTOMATIC_PLUS {
        @Override
        public int getStringId() {
            return R.string.programme_automatic_plus_btn;
        }
    },
    SHIRTS {
        @Override
        public int getStringId() {
            return R.string.programme_shirts_btn;
        }
    },
    DENIM {
        @Override
        public int getStringId() {
            return R.string.programme_denim_btn;
        }
    },
    HYGIENE {
        @Override
        public int getStringId() {
            return R.string.programme_hygiene_btn;
        }
    },
    WARM_AIR {
        @Override
        public int getStringId() {
            return R.string.programme_warm_air_btn;
        }
    },
    GENTLE_SMOOTHING {
        @Override
        public int getStringId() {
            return R.string.programme_gentle_smoothing_btn;
        }
    };

    /**
     * Return the string representation for this enum's entry.
     * @return the string ID for this entry
     */
    public abstract int getStringId();
}
