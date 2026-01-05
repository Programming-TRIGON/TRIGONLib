package frc.trigon.lib.hardware.misc;

import edu.wpi.first.wpilibj2.command.button.Trigger;
import org.littletonrobotics.junction.networktables.LoggedNetworkBoolean;

/**
 * A class that represents a keyboard controller. Used to get input from a keyboard.
 */
public class KeyboardController {
    private final LoggedNetworkBoolean
            esc, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10,
            f11, f12, delete, backtick, one, two, three, four,
            five, six, seven, eight, nine, zero, minus, equals,
            backspace, tab, q, w, e, r, t, y, u, i, o, p, a, s,
            d, f, g, h, j, k, l, semicolon, apostrophe, leftShift,
            z, x, c, v, b, n, m, comma, period,
            rightShift, leftCtrl, leftAlt, rightCtrl,
            left, right, up, down, numpad0, numpad1, numpad2,
            numpad3, numpad4, numpad5, numpad6, numpad7, numpad8,
            numpad9, numpadDecimal;

    /**
     * Construct an instance of a keyboard.
     */
    public KeyboardController() {
        esc = new LoggedNetworkBoolean("/SmartDashboard/keyboard/esc");
        f1 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/f1");
        f2 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/f2");
        f3 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/f3");
        f4 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/f4");
        f5 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/f5");
        f6 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/f6");
        f7 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/f7");
        f8 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/f8");
        f9 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/f9");
        f10 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/f10");
        f11 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/f11");
        f12 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/f12");
        delete = new LoggedNetworkBoolean("/SmartDashboard/keyboard/delete");
        backtick = new LoggedNetworkBoolean("/SmartDashboard/keyboard/`");
        one = new LoggedNetworkBoolean("/SmartDashboard/keyboard/1");
        two = new LoggedNetworkBoolean("/SmartDashboard/keyboard/2");
        three = new LoggedNetworkBoolean("/SmartDashboard/keyboard/3");
        four = new LoggedNetworkBoolean("/SmartDashboard/keyboard/4");
        five = new LoggedNetworkBoolean("/SmartDashboard/keyboard/5");
        six = new LoggedNetworkBoolean("/SmartDashboard/keyboard/6");
        seven = new LoggedNetworkBoolean("/SmartDashboard/keyboard/7");
        eight = new LoggedNetworkBoolean("/SmartDashboard/keyboard/8");
        nine = new LoggedNetworkBoolean("/SmartDashboard/keyboard/9");
        zero = new LoggedNetworkBoolean("/SmartDashboard/keyboard/0");
        minus = new LoggedNetworkBoolean("/SmartDashboard/keyboard/-");
        equals = new LoggedNetworkBoolean("/SmartDashboard/keyboard/=");
        backspace = new LoggedNetworkBoolean("/SmartDashboard/keyboard/backspace");
        tab = new LoggedNetworkBoolean("/SmartDashboard/keyboard/tab");
        q = new LoggedNetworkBoolean("/SmartDashboard/keyboard/q");
        w = new LoggedNetworkBoolean("/SmartDashboard/keyboard/w");
        e = new LoggedNetworkBoolean("/SmartDashboard/keyboard/e");
        r = new LoggedNetworkBoolean("/SmartDashboard/keyboard/r");
        t = new LoggedNetworkBoolean("/SmartDashboard/keyboard/t");
        y = new LoggedNetworkBoolean("/SmartDashboard/keyboard/y");
        u = new LoggedNetworkBoolean("/SmartDashboard/keyboard/u");
        i = new LoggedNetworkBoolean("/SmartDashboard/keyboard/i");
        o = new LoggedNetworkBoolean("/SmartDashboard/keyboard/o");
        p = new LoggedNetworkBoolean("/SmartDashboard/keyboard/p");
        a = new LoggedNetworkBoolean("/SmartDashboard/keyboard/a");
        s = new LoggedNetworkBoolean("/SmartDashboard/keyboard/s");
        d = new LoggedNetworkBoolean("/SmartDashboard/keyboard/d");
        f = new LoggedNetworkBoolean("/SmartDashboard/keyboard/f");
        g = new LoggedNetworkBoolean("/SmartDashboard/keyboard/g");
        h = new LoggedNetworkBoolean("/SmartDashboard/keyboard/h");
        j = new LoggedNetworkBoolean("/SmartDashboard/keyboard/j");
        k = new LoggedNetworkBoolean("/SmartDashboard/keyboard/k");
        l = new LoggedNetworkBoolean("/SmartDashboard/keyboard/l");
        semicolon = new LoggedNetworkBoolean("/SmartDashboard/keyboard/;");
        apostrophe = new LoggedNetworkBoolean("/SmartDashboard/keyboard/'");
        leftShift = new LoggedNetworkBoolean("/SmartDashboard/keyboard/shift");
        z = new LoggedNetworkBoolean("/SmartDashboard/keyboard/z");
        x = new LoggedNetworkBoolean("/SmartDashboard/keyboard/x");
        c = new LoggedNetworkBoolean("/SmartDashboard/keyboard/c");
        v = new LoggedNetworkBoolean("/SmartDashboard/keyboard/v");
        b = new LoggedNetworkBoolean("/SmartDashboard/keyboard/b");
        n = new LoggedNetworkBoolean("/SmartDashboard/keyboard/n");
        m = new LoggedNetworkBoolean("/SmartDashboard/keyboard/m");
        comma = new LoggedNetworkBoolean("/SmartDashboard/keyboard/,");
        period = new LoggedNetworkBoolean("/SmartDashboard/keyboard/.");
        rightShift = new LoggedNetworkBoolean("/SmartDashboard/keyboard/right shift");
        leftCtrl = new LoggedNetworkBoolean("/SmartDashboard/keyboard/ctrl");
        leftAlt = new LoggedNetworkBoolean("/SmartDashboard/keyboard/alt");
        rightCtrl = new LoggedNetworkBoolean("/SmartDashboard/keyboard/right ctrl");
        left = new LoggedNetworkBoolean("/SmartDashboard/keyboard/left");
        right = new LoggedNetworkBoolean("/SmartDashboard/keyboard/right");
        up = new LoggedNetworkBoolean("/SmartDashboard/keyboard/up");
        down = new LoggedNetworkBoolean("/SmartDashboard/keyboard/down");
        numpad0 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/numpad0");
        numpad1 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/numpad1");
        numpad2 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/numpad2");
        numpad3 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/numpad3");
        numpad4 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/numpad4");
        numpad5 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/numpad5");
        numpad6 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/numpad6");
        numpad7 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/numpad7");
        numpad8 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/numpad8");
        numpad9 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/numpad9");
        numpadDecimal = new LoggedNetworkBoolean("/SmartDashboard/keyboard/numpaddecimal");
    }

    public Trigger esc() {
        return new Trigger(esc::get);
    }

    public Trigger f1() {
        return new Trigger(f1::get);
    }

    public Trigger f2() {
        return new Trigger(f2::get);
    }

    public Trigger f3() {
        return new Trigger(f3::get);
    }

    public Trigger f4() {
        return new Trigger(f4::get);
    }

    public Trigger f5() {
        return new Trigger(f5::get);
    }

    public Trigger f6() {
        return new Trigger(f6::get);
    }

    public Trigger f7() {
        return new Trigger(f7::get);
    }

    public Trigger f8() {
        return new Trigger(f8::get);
    }

    public Trigger f9() {
        return new Trigger(f9::get);
    }

    public Trigger f10() {
        return new Trigger(f10::get);
    }

    public Trigger f11() {
        return new Trigger(f11::get);
    }

    public Trigger f12() {
        return new Trigger(f12::get);
    }

    public Trigger delete() {
        return new Trigger(delete::get);
    }

    public Trigger backtick() {
        return new Trigger(backtick::get);
    }

    public Trigger one() {
        return new Trigger(one::get);
    }

    public Trigger two() {
        return new Trigger(two::get);
    }

    public Trigger three() {
        return new Trigger(three::get);
    }

    public Trigger four() {
        return new Trigger(four::get);
    }

    public Trigger five() {
        return new Trigger(five::get);
    }

    public Trigger six() {
        return new Trigger(six::get);
    }

    public Trigger seven() {
        return new Trigger(seven::get);
    }

    public Trigger eight() {
        return new Trigger(eight::get);
    }

    public Trigger nine() {
        return new Trigger(nine::get);
    }

    public Trigger zero() {
        return new Trigger(zero::get);
    }

    public Trigger minus() {
        return new Trigger(minus::get);
    }

    public Trigger equals() {
        return new Trigger(equals::get);
    }

    public Trigger backspace() {
        return new Trigger(backspace::get);
    }

    public Trigger tab() {
        return new Trigger(tab::get);
    }

    public Trigger q() {
        return new Trigger(q::get);
    }

    public Trigger w() {
        return new Trigger(w::get);
    }

    public Trigger e() {
        return new Trigger(e::get);
    }

    public Trigger r() {
        return new Trigger(r::get);
    }

    public Trigger t() {
        return new Trigger(t::get);
    }

    public Trigger y() {
        return new Trigger(y::get);
    }

    public Trigger u() {
        return new Trigger(u::get);
    }

    public Trigger i() {
        return new Trigger(i::get);
    }

    public Trigger o() {
        return new Trigger(o::get);
    }

    public Trigger p() {
        return new Trigger(p::get);
    }

    public Trigger a() {
        return new Trigger(a::get);
    }

    public Trigger s() {
        return new Trigger(s::get);
    }

    public Trigger d() {
        return new Trigger(d::get);
    }

    public Trigger f() {
        return new Trigger(f::get);
    }

    public Trigger g() {
        return new Trigger(g::get);
    }

    public Trigger h() {
        return new Trigger(h::get);
    }

    public Trigger j() {
        return new Trigger(j::get);
    }

    public Trigger k() {
        return new Trigger(k::get);
    }

    public Trigger l() {
        return new Trigger(l::get);
    }

    public Trigger semicolon() {
        return new Trigger(semicolon::get);
    }

    public Trigger apostrophe() {
        return new Trigger(apostrophe::get);
    }

    public Trigger leftShift() {
        return new Trigger(leftShift::get);
    }

    public Trigger z() {
        return new Trigger(z::get);
    }

    public Trigger x() {
        return new Trigger(x::get);
    }

    public Trigger c() {
        return new Trigger(c::get);
    }

    public Trigger v() {
        return new Trigger(v::get);
    }

    public Trigger b() {
        return new Trigger(b::get);
    }

    public Trigger n() {
        return new Trigger(n::get);
    }

    public Trigger m() {
        return new Trigger(m::get);
    }

    public Trigger comma() {
        return new Trigger(comma::get);
    }

    public Trigger period() {
        return new Trigger(period::get);
    }

    public Trigger rightShift() {
        return new Trigger(rightShift::get);
    }

    public Trigger leftCtrl() {
        return new Trigger(leftCtrl::get);
    }

    public Trigger leftAlt() {
        return new Trigger(leftAlt::get);
    }

    public Trigger rightCtrl() {
        return new Trigger(rightCtrl::get);
    }

    public Trigger left() {
        return new Trigger(left::get);
    }

    public Trigger right() {
        return new Trigger(right::get);
    }

    public Trigger up() {
        return new Trigger(up::get);
    }

    public Trigger down() {
        return new Trigger(down::get);
    }

    public Trigger numpad0() {
        return new Trigger(numpad0::get);
    }

    public Trigger numpad1() {
        return new Trigger(numpad1::get);
    }

    public Trigger numpad2() {
        return new Trigger(numpad2::get);
    }

    public Trigger numpad3() {
        return new Trigger(numpad3::get);
    }

    public Trigger numpad4() {
        return new Trigger(numpad4::get);
    }

    public Trigger numpad5() {
        return new Trigger(numpad5::get);
    }

    public Trigger numpad6() {
        return new Trigger(numpad6::get);
    }

    public Trigger numpad7() {
        return new Trigger(numpad7::get);
    }

    public Trigger numpad8() {
        return new Trigger(numpad8::get);
    }

    public Trigger numpad9() {
        return new Trigger(numpad9::get);
    }

    public Trigger numpadDecimal() {
        return new Trigger(numpadDecimal::get);
    }
}
