package client.src.generations;

/**
 * Created by ������ on 11.11.2015.
 */
public class IDGenerator {

    int id;

        /**
         * �����������
         *
         * @param id ID ����������
         */
        public IDGenerator(int id) {
            this.id = id;
        }

        /**
         * �����������
         */
        public IDGenerator() {
            this(0);
        }

        /**
         * ��������� ������ ID
         *
         * @return ��������� ID
         */
        public int getNext() {
            return id++;
        }

        /**
         * ��������� ID
         *
         * @return ���������� ID
         */
        public int getId() {
            return id;
        }



}
