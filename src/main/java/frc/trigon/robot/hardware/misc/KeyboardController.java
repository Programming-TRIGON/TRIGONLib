package frc.trigon.robot.hardware.misc;

import edu.wpi.first.wpilibj2.command.button.Trigger;
import org.littletonrobotics.junction.networktables.LoggedDashboardBoolean;

public class KeyboardController {
    private final LoggedDashboardBoolean
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
        esc = new LoggedDashboardBoolean("keyboard/esc", false);
        f1 = new LoggedDashboardBoolean("keyboard/f1", false);
        f2 = new LoggedDashboardBoolean("keyboard/f2", false);
        f3 = new LoggedDashboardBoolean("keyboard/f3", false);
        f4 = new LoggedDashboardBoolean("keyboard/f4", false);
        f5 = new LoggedDashboardBoolean("keyboard/f5", false);
        f6 = new LoggedDashboardBoolean("keyboard/f6", false);
        f7 = new LoggedDashboardBoolean("keyboard/f7", false);
        f8 = new LoggedDashboardBoolean("keyboard/f8", false);
        f9 = new LoggedDashboardBoolean("keyboard/f9", false);
        f10 = new LoggedDashboardBoolean("keyboard/f10", false);
        f11 = new LoggedDashboardBoolean("keyboard/f11", false);
        f12 = new LoggedDashboardBoolean("keyboard/f12", false);
        delete = new LoggedDashboardBoolean("keyboard/delete", false);
        backtick = new LoggedDashboardBoolean("keyboard/`", false);
        one = new LoggedDashboardBoolean("keyboard/1", false);
        two = new LoggedDashboardBoolean("keyboard/2", false);
        three = new LoggedDashboardBoolean("keyboard/3", false);
        four = new LoggedDashboardBoolean("keyboard/4", false);
        five = new LoggedDashboardBoolean("keyboard/5", false);
        six = new LoggedDashboardBoolean("keyboard/6", false);
        seven = new LoggedDashboardBoolean("keyboard/7", false);
        eight = new LoggedDashboardBoolean("keyboard/8", false);
        nine = new LoggedDashboardBoolean("keyboard/9", false);
        zero = new LoggedDashboardBoolean("keyboard/0", false);
        minus = new LoggedDashboardBoolean("keyboard/-", false);
        equals = new LoggedDashboardBoolean("keyboard/=", false);
        backspace = new LoggedDashboardBoolean("keyboard/backspace", false);
        tab = new LoggedDashboardBoolean("keyboard/tab", false);
        q = new LoggedDashboardBoolean("keyboard/q", false);
        w = new LoggedDashboardBoolean("keyboard/w", false);
        e = new LoggedDashboardBoolean("keyboard/e", false);
        r = new LoggedDashboardBoolean("keyboard/r", false);
        t = new LoggedDashboardBoolean("keyboard/t", false);
        y = new LoggedDashboardBoolean("keyboard/y", false);
        u = new LoggedDashboardBoolean("keyboard/u", false);
        i = new LoggedDashboardBoolean("keyboard/i", false);
        o = new LoggedDashboardBoolean("keyboard/o", false);
        p = new LoggedDashboardBoolean("keyboard/p", false);
        a = new LoggedDashboardBoolean("keyboard/a", false);
        s = new LoggedDashboardBoolean("keyboard/s", false);
        d = new LoggedDashboardBoolean("keyboard/d", false);
        f = new LoggedDashboardBoolean("keyboard/f", false);
        g = new LoggedDashboardBoolean("keyboard/g", false);
        h = new LoggedDashboardBoolean("keyboard/h", false);
        j = new LoggedDashboardBoolean("keyboard/j", false);
        k = new LoggedDashboardBoolean("keyboard/k", false);
        l = new LoggedDashboardBoolean("keyboard/l", false);
        semicolon = new LoggedDashboardBoolean("keyboard/;", false);
        apostrophe = new LoggedDashboardBoolean("keyboard/'", false);
        leftShift = new LoggedDashboardBoolean("keyboard/shift", false);
        z = new LoggedDashboardBoolean("keyboard/z", false);
        x = new LoggedDashboardBoolean("keyboard/x", false);
        c = new LoggedDashboardBoolean("keyboard/c", false);
        v = new LoggedDashboardBoolean("keyboard/v", false);
        b = new LoggedDashboardBoolean("keyboard/b", false);
        n = new LoggedDashboardBoolean("keyboard/n", false);
        m = new LoggedDashboardBoolean("keyboard/m", false);
        comma = new LoggedDashboardBoolean("keyboard/,", false);
        period = new LoggedDashboardBoolean("keyboard/.", false);
        rightShift = new LoggedDashboardBoolean("keyboard/right shift", false);
        leftCtrl = new LoggedDashboardBoolean("keyboard/ctrl", false);
        leftAlt = new LoggedDashboardBoolean("keyboard/alt", false);
        rightCtrl = new LoggedDashboardBoolean("keyboard/right ctrl", false);
        left = new LoggedDashboardBoolean("keyboard/left", false);
        right = new LoggedDashboardBoolean("keyboard/right", false);
        up = new LoggedDashboardBoolean("keyboard/up", false);
        down = new LoggedDashboardBoolean("keyboard/down", false);
        numpad0 = new LoggedDashboardBoolean("keyboard/numpad0", false);
        numpad1 = new LoggedDashboardBoolean("keyboard/numpad1", false);
        numpad2 = new LoggedDashboardBoolean("keyboard/numpad2", false);
        numpad3 = new LoggedDashboardBoolean("keyboard/numpad3", false);
        numpad4 = new LoggedDashboardBoolean("keyboard/numpad4", false);
        numpad5 = new LoggedDashboardBoolean("keyboard/numpad5", false);
        numpad6 = new LoggedDashboardBoolean("keyboard/numpad6", false);
        numpad7 = new LoggedDashboardBoolean("keyboard/numpad7", false);
        numpad8 = new LoggedDashboardBoolean("keyboard/numpad8", false);
        numpad9 = new LoggedDashboardBoolean("keyboard/numpad9", false);
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
