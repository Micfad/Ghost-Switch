package com.example.ghostswitch.otherClass;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentUtil {

    public static void replaceFragment(FragmentManager fragmentManager, Fragment fragment, int containerId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(containerId, fragment);
        transaction.commit();
    }

    public static void removeAllFragments(FragmentManager fragmentManager) {
        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment != null) {
                fragmentManager.beginTransaction().remove(fragment).commit();
            }
        }
    }

    public static void restartFragment(Fragment fragment) {
        if (fragment.getActivity() != null) {
            FragmentManager fragmentManager = fragment.getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.detach(fragment);
            transaction.attach(fragment);
            transaction.commit();
        }
    }
}
