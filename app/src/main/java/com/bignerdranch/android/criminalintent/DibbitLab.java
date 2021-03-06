package com.bignerdranch.android.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by user on 10/8/2015.
 */
public class DibbitLab {
    private static DibbitLab sDibbitLab;

    private List<Dibbit> mDibbits;

    private DibbitLab(Context context) {
        mDibbits = new ArrayList<>();
//        for(int i = 0; i < 100; i++) {
//            Dibbit dibbit = new Dibbit();
//            dibbit.setTitle("Dibbit #" + i);
//            dibbit.setSolved(i%2 == 0);  //every other one
//            mDibbits.add(dibbit);
//        }
    }

    public static DibbitLab get(Context context) {
        if(sDibbitLab == null) {
            sDibbitLab = new DibbitLab(context);
        }
        return sDibbitLab;
    }

    public List<Dibbit> getDibbits() {
        return mDibbits;
    }

    public Dibbit getDibbit(UUID id) {
        for(Dibbit dibbit : mDibbits) {
            if(dibbit.getId().equals(id)) {
                return dibbit;
            }
        }
        return null;
    }

    public void addDibbit(Dibbit c){
        mDibbits.add(c);
    }

    public void deleteDibbit(Dibbit c){
        mDibbits.remove(c);
    }
}

