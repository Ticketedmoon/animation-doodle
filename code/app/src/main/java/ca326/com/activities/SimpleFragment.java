package ca326.com.activities;

import android.support.annotation.NonNull;

import com.google.blockly.android.AbstractBlocklyFragment;
import com.google.blockly.android.codegen.CodeGenerationRequest;
import com.google.blockly.android.codegen.LoggingCodeGeneratorCallback;
import com.google.blockly.model.DefaultBlocks;

import java.util.Arrays;
import java.util.List;

/**
 * Simple demonstration of using Blockly in a Fragment via the {@link AbstractBlocklyFragment}.
 */
public class SimpleFragment extends AbstractBlocklyFragment {
    private static final String TAG = "SimpleActivity";

    // This fragment uses the same save locations as the SimpleActivity demo.
    private static final String SAVE_FILENAME = "simple_workspace.xml";
    private static final String AUTOSAVE_FILENAME = "simple_workspace_temp.xml";

    // Add custom blocks to this list.
    private static final List<String> BLOCK_DEFINITIONS = DefaultBlocks.getAllBlockDefinitions();
    private static final List<String> JAVASCRIPT_GENERATORS = Arrays.asList(
            // Custom block generators go here. Default blocks are already included.
    );

    CodeGenerationRequest.CodeGeneratorCallback mCodeGeneratorCallback = null;

    @NonNull
    @Override
    protected List<String> getBlockDefinitionsJsonPaths() {
        return BLOCK_DEFINITIONS;
    }

    @NonNull
    @Override
    protected String getToolboxContentsXmlPath() {
        // Replace with a toolbox that includes application specific blocks.
        return DefaultBlocks.TOOLBOX_PATH;
    }

    @NonNull
    @Override
    protected List<String> getGeneratorsJsPaths() {
        return JAVASCRIPT_GENERATORS;
    }

    @NonNull
    @Override
    protected CodeGenerationRequest.CodeGeneratorCallback getCodeGenerationCallback() {
        if (mCodeGeneratorCallback == null) {
            // Late initialization since Context is not available at construction time.
            mCodeGeneratorCallback = new LoggingCodeGeneratorCallback(getContext(), TAG);
        }
        return mCodeGeneratorCallback;
    }

    // START SHANE ADDITIONS
    @Override
    public void onInitBlankWorkspace() {
        // Initialize available variable names.
        getController().addVariable("item");
    }

    // END

    /**
     * Optional override of the save path, since this demo Activity has multiple Blockly
     * configurations.
     * @return Workspace save path used by SimpleActivity and SimpleFragment.
     */
    @Override
    @NonNull
    protected String getWorkspaceSavePath() {
        return SAVE_FILENAME;
    }

    /**
     * Optional override of the auto-save path, since this demo Activity has multiple Blockly
     * configurations.
     * @return Workspace auto-save path used by SimpleActivity and SimpleFragment.
     */
    @Override
    @NonNull
    protected String getWorkspaceAutosavePath() {
        return AUTOSAVE_FILENAME;
    }
}

