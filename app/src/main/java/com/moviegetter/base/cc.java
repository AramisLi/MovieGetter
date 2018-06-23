package com.moviegetter.base;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Aramis
 * Date:2018/6/22
 * Description:
 */
public class cc {
    public static void main(String[] args){
        String a ="�\b����������\u0002���}�s\u0013G���߭���w�]��H��<$l}�<+����\u0002��nQ\u00145�\u0006I ��F\u0006gk�\u0018\u0003�\u0017\u0086\uD93F\uDC8D�\u001B\u001B��\u000F�����dC\u0012�\t\t\u001BH�n�vϨ{4�d�`\u001D�6���������g���֨�g\u0007~���o�\u001C\u0004Q�5\u000E���_\u001D�\u000F\u0018\u000F��F�ϲ\u0007�\u001F������7~\u0005x/\u0007��4#\u001D3c\tC����\u007Fe��\u00135�d�eϝ;�='z\u0013�\b{�({\u001E��Q�b�c����0\u0013��O��\u001AϷƍts\u00059��\uFDCB�`]\u000B��o\u001C<�\u0017 �G���X[���a��9ޞ�AȾhfL�ɢ�{@(��Һ�\u001Ci\u0011D^�d�13�\u0007{\u001F.w\u0002\u0016�}�\u001FC�S����o�{�\f��������&�\u0002㚝\u001A0��?���h\u0006\u0018Z��|Vo?�H�ӕKa�k��J�}14�;���]\u001D-\n" +
                "    \n" +
                "    ��P*�DzC��1�,�����\f\u001BJ��p�i�^�b@J�73i�=����n2\u0004�(��\u0002�f\u0016-�\u0003SKEtؒS-q�8k��E���\u0013��`S8�\u0006b�f\u0006��)��\bŵ44\u0012�\u00163`f�\u0017l�pSbFX?�-��j�?��ӟ�\u001F�N\\*\u0016���S�֒�6j�X�hf컰$���tl���G��t*\u0004+=�\u0016XE��3iT�}\u000FՊ~�XX\u0003���89�ӬV��T�c&N��c0Y�k��G�������F�p;�ʛo�t�F�r��\u001Fl�@Yu�s�\u001A}��\u0010\u0015��\f�%p���=4�^P`\"Ӫ�\\\u0002'��s\u001B\t,)��E5�Uxfu�j��f\u001B\u001Bm��JKf�^�����r��v��oy��\u000B�݅\u007F�w�v�7\u0013F�=&p�X64��_Zݸ��q~[�{0���v�0,غN�}��6�r����V�Ulya��z(ֶ�0�\u0005�+s�1\u0012'\tbQ��\u0007�\u00054��\t\u0001%rv1 a����f�֦�\u000F��9ؖ�9�~��Sh\u001E�\u001B\u000E\u001F��\u0012���76�\fT�X�\u0006j�k��<՝�\u074C�\u0014L���Lpxy���jwՎ9J@-�o�̌��^��mѣZ[,�jnȤ⍻��i-\u00137wE\u0013�zR��\u001B��`�|\u001Df\u001D�Y�\u001B�6�ޓ��L�h<�����=\f�jhfp\u0015\u0001P�\n" +
                "    &x듅�\u000Bϖzp'�G{&\u001E\\��\u0003P��\n" +
                "    �+}�\u0005&�>�\u0017'�8\u0017\u000B������\\�\u00054���\u000EBz<�Nj��\u0011ifx\u0006�$Ra=��p�u+����-\u001F\u0003,\t\u0010�q?g��(��L�) �\u001BиL�F�\u0013\u001F��z�\u0017q��\n" +
                "    �?Vs`��}\u000F^����E~~�\u001B�=\u001B�r~<�`��ֵT(��K;ŮS]R3��<��5ݹ�-�_\u001C�C���]����k�\u0011�\u0004\\q6h�<\u00187��4�`=kH\tD㥞�\u0011b�����Ґ\u001Bf��9\u0003�m�x�ƄȔg�\u0019���{3�=��ړ�\u0007\u001E��\u000Ej\tj��d>\u000F�'�<�\u001E�5Z��r��2F�5!�8��t�l�Z��)-�\\�dp������č�B�A\u0013��;Z.��OKXq�G�2\\\u001C��!\u0006�:rN�\u0019�c@[q����&�3ԕ��\u0013\u0011`�ɸn�=\u001E�\u000F<衜��\u0010\u00187�\u00104\u0012J�\u0013\u0010�됌�1��T\u0010؍�R���B\u000FZ?�!f8h\u0001��*�:�\u0018�i\u0001>y\u001E�:`�t\"\u001E\u000B3�F�'ä%{=���.W�]X\u001E�\n" +
                "    \u0003?��k�?x�!�\u001A:(\u001F�{P�\u001A�ђN�:P[��\u001E:pH��Z�ׄ:W�:uY��\u0017a��\u0004Kk�1Ym�U�wL��MVy��c�-l�3�u���\u0016���*�\b�\u0011\u0006���Kf�%�k�_���2���cU�q�7v��\u001B:?5�\u0014��.ǩ�RU_T�x��9��$��/:�0S����Tv��ݵg\u001D}3W���\u007F�u\u0005٬�%��\u0014���!�\u0002�\u0012���7\n" +
                "    ݹ��|Gϓ���g5E�\u0001T\u0014)�HU�D�����ߗnu�ݹ23�\u07B9R�D\u0001I�y����ч��+]�\u000B�VG:��\u001D��\u07FC�D��K��õ��K7��\u001D˾m��\u001E�\u001C\u0012���n��\u0013�=�Vǥ����� �@=\u001D�d$RT]\"Q���w�g���w\u0007ߪ��\u0012\u0012)�E�^\u0011�|ud���7n��D�l\be�\u001A�\u001A}8�iG���ku�D\u0006�U_�H[��ϻ'�\u0007g\u000Bu�\u0012�t\u001B����*����wbq��V�\"�D�R\u0015��\u007F39\u0005�s�\u0005D�;n\u000F��\u000Bck}+�O;�\u0007��#R\u0010�HN� r<7����\u001B��x�\u001E���}�{\b��mK\u001D�:t�p�g�Dd�s���\u0005����wvy���=\u007Fq�T�`�e�cEA\u0015K\u0014�\u07FE<�\u0017����ů���?�\u001F�nW\u0011\u0018ь���X�\u001C\u0012�G�\u008A\u001CǗЇC\"�h,����T}b9\u0015�]P\u0014Q)\u0011;9��ϝw�pG0�X[&�8�)��ME͜��~\u000BK,,/ܯ��\u0012��\u0002\u001C�R��R�C\u007F\u001B�_\u007F2��N�<|�\u000B>(}]k�����G�\u07B5�z�r~�^V\u00108\u001F�1Qfik\u0017ǖ����z��H� �KĢ�v�]\u007F���^�^�w���$�K��\u001E��߱�)�U/�3�y�\f�\u0004�WB�\u0005X���ȁt(��ǑCh$@���Č�#�\u0015/풥�#\u000Ey�vU�\u05EB���\u001A\u00107���\u0003\u001F��/<G��z�P���?[\u0001j�\u0002*�\u001C��G�+�X\u001EyL1n\n";
        String a1="�\b����������\u0002���}�s\u0013G���߭���w�]��H��<$l}�";
        try {
//            String b=URLEncoder.encode(a, "ISO-8859-1");
//            String c=URLDecoder.decode(b,"UTF-8");
//            String d=URLDecoder.decode(b,"gb2312");
//            String e=URLDecoder.decode(b,"gbk");
//
////            System.out.print(b);
////            System.out.print(c);
////            System.out.print(d);
////            System.out.print(e);
//
//            String f=URLEncoder.encode(a1, "ISO-8859-1");
//            String g=URLDecoder.decode(f,"GB2312");
//            System.out.print(f);
//            System.out.print(g);
//
//            byte[] h = a.getBytes("ISO-8859-1");//编码
//            String sa = new String(h, "gb2312");
//            System.out.print(sa);
//
//            SortedMap<String, Charset> stringCharsetSortedMap =
//                    Charset.availableCharsets();
//            System.out.print(stringCharsetSortedMap);

//            OKhttpUtils oKhttpUtils=new OKhttpUtils();
//            Response response = oKhttpUtils.fetch("http://www.dytt8.net/html/gndy/dyzz/index.html", "GET", null, null, false);
//            System.out.print(response.code());
//            System.out.print(response.body().string());
            Request request = new Request.Builder().url("http://www.dytt8.net/html/gndy/dyzz/index.html").get().build();
            OkHttpClient client = new OkHttpClient.Builder().build();
            Call call = client.newCall(request);
            Response response = call.execute();
//            System.out.print(response.code());
//            System.out.print(response.body().string());
//            String html=response.body().string();
//            String c=new String(html.getBytes("GB2312"),"GBK");
//            System.out.print(c);
            String d=new String(response.body().bytes(),"GB2312");
            System.out.print("ddd");
            System.out.print(d);
            System.out.print(response.body().bytes().length);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
