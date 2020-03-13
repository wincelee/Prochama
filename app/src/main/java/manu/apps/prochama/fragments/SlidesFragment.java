package manu.apps.prochama.fragments;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import manu.apps.prochama.R;
import manu.apps.prochama.adapters.SlidesPagerAdapter;
import manu.apps.prochama.classes.PreferenceManager;

public class SlidesFragment extends Fragment implements View.OnClickListener {

    private SlidesViewModel slidesViewModel;

    private ViewPager viewPager;
    private int [] layouts = {R.layout.slide1,R.layout.slide2, R.layout.slide3};
    private SlidesPagerAdapter slidesPagerAdapter;

    private LinearLayout dotsLayout;
    private ImageView[] dots;

    private Button btnSkip, btnNext;

    public static SlidesFragment newInstance() {
        return new SlidesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.slides_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        slidesViewModel = new ViewModelProvider(this).get(SlidesViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (new PreferenceManager(getActivity()).checkPreference())
        {
            login(view);
        }else {
            if (Build.VERSION.SDK_INT >=19)
            {
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
            else
            {
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }

        viewPager = view.findViewById(R.id.vp_slides);
        dotsLayout = view.findViewById(R.id.dots_layout);
        btnSkip = view.findViewById(R.id.btn_skip);
        btnNext = view.findViewById(R.id.btn_next);



        btnSkip.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        slidesPagerAdapter = new SlidesPagerAdapter(layouts, getActivity());
        viewPager.setAdapter(slidesPagerAdapter);

        createDots(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                createDots(position);

                if (position==layouts.length-1)
                {
                    btnNext.setText(R.string.next);
                    btnSkip.setVisibility(View.INVISIBLE);
                }
                if (position==layouts.length-2)
                {
                    btnNext.setText(R.string.next);
                    btnSkip.setVisibility(View.INVISIBLE);
                }
                else
                {
                    btnNext.setText(R.string.finish);
                    btnNext.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                loadNextSlide(v);
                break;
            case R.id.btn_skip:
                login(v);
                new PreferenceManager(getActivity()).writePreference();
                break;
        }
    }

    private void createDots(int current_position)
    {
        if (dotsLayout != null)
            dotsLayout.removeAllViews();

        dots = new ImageView[layouts.length];

        for (int i = 0; i < layouts.length; i++ )
        {
            dots[i] = new ImageView(getActivity());
            if (i == current_position)
            {
                dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.active_dots));
            }
            else
            {
                dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.default_dots));
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(4,0,4,0);

            dotsLayout.addView(dots[i], params);
        }
    }

    private void loadNextSlide(View view){
        int next_slide = viewPager.getCurrentItem()+1;

        if (next_slide<layouts.length)
        {
            viewPager.setCurrentItem(next_slide);
        }
        else
        {
            login(view);
            new PreferenceManager(getActivity()).writePreference();
        }
    }
    private void login(View view) {

        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        Navigation.findNavController(view).navigate(R.id.action_slides_to_dashboard_fragment);
    }
}
