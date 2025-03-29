package comp3350.gymbuddy.objects;

    /**
     * The Tag class represents a tag that can be associated with a workout item.
     * Each tag has a type, a name (represented by a string name), a text color (represented by a hex code ),
     * and a background color (represented by a hex code ).
     */
   public class Tag {
        /**
         * Enum to define the different types of tags.
         */
        public enum TagType {
            DIFFICULTY, MUSCLE_GROUP, EXERCISE_TYPE, EQUIPMENT
        }

        private final TagType type; // The type of the tag
        private final String name; // String  name of the tag
        private final String textColor; //  hex for the text color of the tag
        private final String bgColor; //  hex for the background color of the tag

        /**
         * Constructor to initialize a Tag object.
         *
         * @param type The type of the tag
         * @param name string  name of the tag
         * @param textColor hex code for the text color of the tag
         * @param bgColor hex code for the background color of the tag
         */
        public Tag(TagType type, String name, String textColor, String bgColor) {
            this.type = type;
            this.name= name;
            this.textColor = textColor;
            this.bgColor = bgColor;
        }

        /**
         * Gets the type of the tag.
         *
         * @return The type of the tag
         */
        public TagType getType() {
            return type;
        }

        /**
         * Gets the string name of the tag.
         *
         * @return The string name of the tag
         */
        public String getName() {
            return name;
        }

        /**
         * Gets the hex code for the text color of the tag.
         *
         * @return The hex code for the text color of the tag
         */
        public String getTextColor() {
            return textColor;
        }

        /**
         * Gets the hex code for the background color of the tag.
         *
         * @return The hex code for the background color of the tag
         */
        public String getBgColor() {
            return bgColor;
        }
    }