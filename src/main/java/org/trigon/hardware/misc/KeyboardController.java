package org.trigon.hardware.misc;

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
            numpad9;

    /**
     * Construct an instance of a device.
     */
    public KeyboardController() {
        esc = new LoggedNetworkBoolean("/SmartDashboard/keyboard/esc", false);
        f1 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/f1", false);
        f2 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/f2", false);
        f3 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/f3", false);
        f4 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/f4", false);
        f5 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/f5", false);
        f6 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/f6", false);
        f7 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/f7", false);
        f8 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/f8", false);
        f9 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/f9", false);
        f10 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/f10", false);
        f11 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/f11", false);
        f12 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/f12", false);
        delete = new LoggedNetworkBoolean("/SmartDashboard/keyboard/delete", false);
        backtick = new LoggedNetworkBoolean("/SmartDashboard/keyboard/`", false);
        one = new LoggedNetworkBoolean("/SmartDashboard/keyboard/1", false);
        two = new LoggedNetworkBoolean("/SmartDashboard/keyboard/2", false);
        three = new LoggedNetworkBoolean("/SmartDashboard/keyboard/3", false);
        four = new LoggedNetworkBoolean("/SmartDashboard/keyboard/4", false);
        five = new LoggedNetworkBoolean("/SmartDashboard/keyboard/5", false);
        six = new LoggedNetworkBoolean("/SmartDashboard/keyboard/6", false);
        seven = new LoggedNetworkBoolean("/SmartDashboard/keyboard/7", false);
        eight = new LoggedNetworkBoolean("/SmartDashboard/keyboard/8", false);
        nine = new LoggedNetworkBoolean("/SmartDashboard/keyboard/9", false);
        zero = new LoggedNetworkBoolean("/SmartDashboard/keyboard/0", false);
        minus = new LoggedNetworkBoolean("/SmartDashboard/keyboard/-", false);
        equals = new LoggedNetworkBoolean("/SmartDashboard/keyboard/=", false);
        backspace = new LoggedNetworkBoolean("/SmartDashboard/keyboard/backspace", false);
        tab = new LoggedNetworkBoolean("/SmartDashboard/keyboard/tab", false);
        q = new LoggedNetworkBoolean("/SmartDashboard/keyboard/q", false);
        w = new LoggedNetworkBoolean("/SmartDashboard/keyboard/w", false);
        e = new LoggedNetworkBoolean("/SmartDashboard/keyboard/e", false);
        r = new LoggedNetworkBoolean("/SmartDashboard/keyboard/r", false);
        t = new LoggedNetworkBoolean("/SmartDashboard/keyboard/t", false);
        y = new LoggedNetworkBoolean("/SmartDashboard/keyboard/y", false);
        u = new LoggedNetworkBoolean("/SmartDashboard/keyboard/u", false);
        i = new LoggedNetworkBoolean("/SmartDashboard/keyboard/i", false);
        o = new LoggedNetworkBoolean("/SmartDashboard/keyboard/o", false);
        p = new LoggedNetworkBoolean("/SmartDashboard/keyboard/p", false);
        a = new LoggedNetworkBoolean("/SmartDashboard/keyboard/a", false);
        s = new LoggedNetworkBoolean("/SmartDashboard/keyboard/s", false);
        d = new LoggedNetworkBoolean("/SmartDashboard/keyboard/d", false);
        f = new LoggedNetworkBoolean("/SmartDashboard/keyboard/f", false);
        g = new LoggedNetworkBoolean("/SmartDashboard/keyboard/g", false);
        h = new LoggedNetworkBoolean("/SmartDashboard/keyboard/h", false);
        j = new LoggedNetworkBoolean("/SmartDashboard/keyboard/j", false);
        k = new LoggedNetworkBoolean("/SmartDashboard/keyboard/k", false);
        l = new LoggedNetworkBoolean("/SmartDashboard/keyboard/l", false);
        semicolon = new LoggedNetworkBoolean("/SmartDashboard/keyboard/;", false);
        apostrophe = new LoggedNetworkBoolean("/SmartDashboard/keyboard/'", false);
        leftShift = new LoggedNetworkBoolean("/SmartDashboard/keyboard/shift", false);
        z = new LoggedNetworkBoolean("/SmartDashboard/keyboard/z", false);
        x = new LoggedNetworkBoolean("/SmartDashboard/keyboard/x", false);
        c = new LoggedNetworkBoolean("/SmartDashboard/keyboard/c", false);
        v = new LoggedNetworkBoolean("/SmartDashboard/keyboard/v", false);
        b = new LoggedNetworkBoolean("/SmartDashboard/keyboard/b", false);
        n = new LoggedNetworkBoolean("/SmartDashboard/keyboard/n", false);
        m = new LoggedNetworkBoolean("/SmartDashboard/keyboard/m", false);
        comma = new LoggedNetworkBoolean("/SmartDashboard/keyboard/,", false);
        period = new LoggedNetworkBoolean("/SmartDashboard/keyboard/.", false);
        rightShift = new LoggedNetworkBoolean("/SmartDashboard/keyboard/right shift", false);
        leftCtrl = new LoggedNetworkBoolean("/SmartDashboard/keyboard/ctrl", false);
        leftAlt = new LoggedNetworkBoolean("/SmartDashboard/keyboard/alt", false);
        rightCtrl = new LoggedNetworkBoolean("/SmartDashboard/keyboard/right ctrl", false);
        left = new LoggedNetworkBoolean("/SmartDashboard/keyboard/left", false);
        right = new LoggedNetworkBoolean("/SmartDashboard/keyboard/right", false);
        up = new LoggedNetworkBoolean("/SmartDashboard/keyboard/up", false);
        down = new LoggedNetworkBoolean("/SmartDashboard/keyboard/down", false);
        numpad0 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/numpad0", false);
        numpad1 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/numpad1", false);
        numpad2 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/numpad2", false);
        numpad3 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/numpad3", false);
        numpad4 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/numpad4", false);
        numpad5 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/numpad5", false);
        numpad6 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/numpad6", false);
        numpad7 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/numpad7", false);
        numpad8 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/numpad8", false);
        numpad9 = new LoggedNetworkBoolean("/SmartDashboard/keyboard/numpad9", false);
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
}
