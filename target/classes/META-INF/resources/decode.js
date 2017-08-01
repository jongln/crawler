//大众点评经纬度格式解密
function decode(C) {
        var digi=16;
        var add= 10;
        var plus=7;
        var cha=36;
        var I = -1;
        var H = 0;
        var B = "";
        var J = C.length;
        var G = C.charCodeAt(J - 1);
        C = C.substring(0, J - 1);
        J--;
        for (var E = 0; E < J; E++) {
            var D = parseInt(C.charAt(E), cha) - add;
            if (D >= add) {
                D = D - plus
            }
            B += (D).toString(cha);
            if (D > H) {
                I = E;
                H = D
            }
        }
        var A = parseInt(B.substring(0, I), digi);
        var F = parseInt(B.substring(I + 1), digi);
        var L = (A + F - parseInt(G)) / 2;
        var K = (F - L) / 100000;
        L /= 100000;
        return {
            lat: K,
            lng: L
        }
}
