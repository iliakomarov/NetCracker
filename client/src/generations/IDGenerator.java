package client.src.generations;

/**
 * Created by Степан on 11.11.2015.
 */
public class IDGenerator {

    int id;

        /**
         * Конструктор
         *
         * @param id ID генератора
         */
        public IDGenerator(int id) {
            this.id = id;
        }

        /**
         * Конструктор
         */
        public IDGenerator() {
            this(0);
        }

        /**
         * Получение нового ID
         *
         * @return Получение ID
         */
        public int getNext() {
            return id++;
        }

        /**
         * Получение ID
         *
         * @return Возвращает ID
         */
        public int getId() {
            return id;
        }



}
